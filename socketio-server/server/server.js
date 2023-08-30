import cluster from 'cluster';
import os from 'os';
import http from 'http';
import { Server as SocketIOServer } from 'socket.io';
import redisAdapter from 'socket.io-redis';
import { createClient } from 'redis';
import AWS from 'aws-sdk';
import e from 'express';
import { isErrored } from 'stream';

const numCPUs = os.cpus().length;

if (cluster.isMaster) {
  const workers = [];

  for (let i = 0; i < 4; i++) {
    const worker = cluster.fork();
    workers.push(worker);
  }

  const killWorker = (workerIndex) => {
    if (workers[workerIndex]) {
      console.log(`Killed worker`)
      workers[workerIndex].kill();
    }
  };

  cluster.on('exit', (worker, code, signal) => {
    console.log(`Worker ${worker.process.pid} died`);
  });
} else {
  const server = http.createServer();
  const io = new SocketIOServer(server, {
    cors: {
      origin: ['http://localhost:3000', 'http://localhost:3001'],
    },
  });

  const redisClient = createClient({
    host: 'localhost',
    port: 6379,
  });

  io.adapter(redisAdapter(redisClient));
  const sqs = new AWS.SQS({
    region: 'us-east-1',
    accessKeyId: 'AKIA3B3WWQCBT3KXCIGP',
    secretAccessKey: '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',
  });
  
  const queueUrl = 'https://sqs.us-east-1.amazonaws.com/759921279107/MyQueue';

  const receiveMessages = async () => {
    const params = {
      QueueUrl: queueUrl,
      MaxNumberOfMessages: 10,
    };
    
    try {
      const data = await sqs.receiveMessage(params).promise();
      if (data.Messages) {
        for (const message of data.Messages) {
          try {
            console.log("Received message:", message.Body);
            const { to } = JSON.parse(message.Body);

            io.to(to.toString()).emit('incoming-message', 'received message');
            const deleteParams = {
              QueueUrl: queueUrl,
              ReceiptHandle: message.ReceiptHandle,
            };
            await sqs.deleteMessage(deleteParams).promise();
            console.log("Deleted message:", message.MessageId);
          } catch (innerError) {
            console.error("Error processing message:", innerError);
          }
        }
      }
    } catch (error) {
      console.error("Error receiving messages:", error);
    }
  
    setTimeout(receiveMessages, 5000);
  };

  receiveMessages();

  io.on('connection', (socket) => {
    const userId = socket.handshake.query.user_id;
    const workerId = cluster.worker.id;
    
    console.log(`Worker ${workerId} is handling user ${userId}'s connection`);
    socket.join(userId);

    socket.on('joinChannel', (groupId) => {
      console.log(`Joining ${groupId} room`)
      socket.join(groupId)
    })
    
    socket.on('disconnect', () => {
      socket.leave(userId);
    });
  });

  const PORT = process.env.PORT || 3003;

  server.listen(PORT, () => {
    console.log(`Worker ${cluster.worker.id} listening on port ${PORT}`);
  });
}

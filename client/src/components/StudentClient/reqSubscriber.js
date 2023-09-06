import * as zmq from "zeromq";

export const createZmqSubscriber = (name, setQueue, setNotification) => {
  // Create ZMQ subscriber socket
  const subscriber = new zmq.Subscriber();

  try {
    subscriber.connect("tcp://ds.iit.his.se:5555");
    subscriber.subscribe("queue");
    subscriber.subscribe(name); 

    subscriber.on("message", (topic, message) => {
      // Handle incoming messages based on topic
      if (topic.toString() === "queue") {
        setQueue(JSON.parse(message.toString()));
      } else if (topic.toString() === name) {
        setNotification(JSON.parse(message.toString()).message);
      }
    });
  } catch (error) {
    console.error("Failed to initialize ZMQ subscriber:", error);
  }

  return subscriber;
};
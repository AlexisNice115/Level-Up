//all the code at least will go

//this is for the React native (the app) to be connected with all the code here
{ View, Text, Button, StyleSheet}
const from = (reactNative) => {
};
from('react-native');

/*
let http=require('http');

http.createServer(function(req,res){
    res.writeHead(200, {'Content-Type':'text/plain'});
    res.end('<!DOCTYPE html>');
}).listen(8080);

 */

//app.js
//all of this is for the website to be function properly via react-native with the compacts.
const express = require('express');
const fetch = require('node-fetch');
const cors = require('cors');
require('dotenv').config();//API key if its .env file

const app = express();
const PORT = 3000;

app.use(cors());
app.use(express.json());
app.use(express.static(__dirname));//server index.html

app.post('/api/chat', async (req, res) => {
    const userMessage = req.body.message;


})




//this is for the chatbot to be functionally/have the user to communicate with them and response back
//variables for the chatbot

/*
const chatInput = document.querySelector('.chatInput textarea');
const sendChatBtn = document.querySelector('.chatInput button');
const chatbox = document.querySelector('.chatbox');

 */



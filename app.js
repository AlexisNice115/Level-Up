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

const DB_PATH = path.join(__dirname, 'db.json');

function readDB() {
    try {
        return JSON.parse(fs.readFileSync(DB_PATH, 'utf-8'));
    } catch (err) {
        return { users: [], newsletter: [], recommendations: [] }; // JSON databases for users, newsletter subscribers, and recommendations
    }
}

function saveDB(data) {
    fs.mkdirSync(path.dirname(DB_PATH), { recursive: true });
    fs.writeFileSync(DB_PATH, JSON.stringify(data, null, 2));
}

const db = readDB();

app.get('/api/health', (req, res) => res.json({ status: 'OK' }));
app.get('/api/recommendations', (req, res) => res.json(db.recommendations || []));

app.post('/api/chat', async (req, res) => {
    const userMessage = req.body.message;
    if (!userMessage)
        return res.status(400).json({ error: 'Message is required' });
    res.json({ reply: `You said: ${userMessage}` });
})

//this is for the chatbot to be functionally/have the user to communicate with them and response back
//variables for the chatbot

/*
const chatInput = document.querySelector('.chatInput textarea');
const sendChatBtn = document.querySelector('.chatInput button');
const chatbox = document.querySelector('.chatbox');

 */

app.post('/api/signup',  (req, res) => {
        const { email, password, name } = req.body || {};
        if (!email || !password)
            return res.status(400).json({ error: 'Email and password are required' });
        db.users = db.users || [];
        if (db.users.find(user => user.email === email))
            return res.status(400).json({ error: 'User already exists' });
        db.users.push({ email, password, name: name || null, createdAt: new Date().toISOString() });
        saveDB(db);
        const user = { email, name };
        res.status(201).json({ user });
    });

app.post('/api/login', (req, res) => {
    const { email, password } = req.body || {};
    if (!email || !password)
        return res.status(400).json({ error: 'Email and password are required' });
    const user = (db.users || []).find(user => user.email === email && user.password === password);
    if (!user)
        return res.status(401).json({ error: 'Invalid email or password' });
    if (!(db.users || []).find(user.email === email)) 
        return res.status(404).json({ error: 'User not found' });
    res.json({ user: { email: user.email, name: user.name } });
});

app.post('/api/newsletter', (req, res) => {
    const { email } = req.body || {};
    if (!email)
        return res.status(400).json({ error: 'Email is required' });
    db.newsletter = db.newsletter || [];

    if (db.newsletter.includes(email))
        return res.status(400).json({ error: 'Email already subscribed' });
    else
        db.newsletter.push(email);
        res.json({ message: 'Subscribed successfully' });
        saveDB(db);
    res.json({ subscribed: true})
});

// unsubscribe endpoint (remove comment once implemented in the frontend)

// app.post('/api/newsletter/unsubscribe', (req, res) => {
//     const { email } = req.body || {};
//     if (!email)
//         return res.status(400).json({ error: 'Email is required' });
//     db.newsletter = db.newsletter || [];
//     db.newsletter = db.newsletter.filter(e => e !== email);
//     saveDB(db);
//     res.json({ unsubscribed: true });
// });

// TODO: Add recommendations endpoint and code to manage recommendations

app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});



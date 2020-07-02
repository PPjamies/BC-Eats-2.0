var functions = require('firebase-functions');
let admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

//get the month in MMM, dd yyyy format
var monthNames = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
var date = new Date(); 
var dateString = monthNames[date.getMonth()] + " " + date.getDay() + ", " + date.getFullYear();

const databaseRef = "/" + dateString + "/{postID}";
exports.sendPush = functions.database.ref("/posts/{dateID}/{postID}").onWrite((change,context) => {

    functions.logger.log("What is the database ref: " + databaseRef);
    let postStateChanged = false;
	let postCreated = false;
    let postData = change.after.val(); 

    if (!change.before.exists()) {
        postCreated = true;
    }

    if (!postCreated && change.data.changed()) {
        postStateChanged = true;
    }

    let msg = 'A project state was changed';

		if (postCreated || postStateChanged) {
            msg = `${postData.title} in ${postData.building} ${postData.room}`;
		}

    return loadUsers().then(users => {
        let tokens = [];
        for (let user of users) {
            tokens.push(user.pushToken);
        }

        let payload = {
            data: {
                title: 'BC Eats 2.0',
                body: msg,
                sound: 'default',
                badge: '1'
            }
        };

        return admin.messaging().sendToDevice(tokens, payload);
    });
});

function loadUsers() {
    let dbRef = admin.database().ref('/users');
    let defer = new Promise((resolve, reject) => {
        dbRef.once('value', (snap) => {
            let data = snap.val();
            let users = [];
            for (var property in data) {
                users.push(data[property]);
            }
            resolve(users);
        }, (err) => {
            reject(err);
        });
    });
    return defer;
}
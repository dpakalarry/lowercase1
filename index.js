let name;
let type;
let accountNum;
let amount;
let transactionId;

function completeTransaction() {
	var config = {
		projectId: "atm-server-953c5",
		//messagingSenderId: "678522738824"
		apiKey: "AIzaSyCs_6BTTUoPm1zZJeB5-M6PoWozSbdATnA",
		authDomain: "atm-server-953c5.firebaseapp.com",
		databaseURL: "https://atm-server-953c5.firebaseio.com/",
		storageBucket: "atm-server-953c5.appspot.com"
	}
	let fb = firebase.initializeApp(config);

	console.log(fb.database()
		// Get entire Db contents
		.ref())
	//TODO: Remove transaction from db and update balance in db

	// Get current balance and update with transaction amount
	fb.database().ref().once('value').then(snapshot => {
		let entireDbAsJson = snapshot.val();
		let dbObj = JSON.parse(entireDbAsJson);

		let newBalance = dbObj['Bank Accounts'][accountNum]['Balance'];
		type === 'w' ? newBalance -= amount : newBalance += amount;

		fb.database().ref(`Database/${ accountNum }/Balance`).set(newBalance);


		return null;
	});

	// Remove transaction from db
	fb.database().ref(`Transactions/${ transactionId }`).set(null);
}

var nameVal;
var typeVal;
var actVal;
var amtVal;

function  parseParams(){

nameVal = document.getElementById("NamVal").innerHTML;
typeVal = document.getElementById("TTVal").innerHTML;
actVal = document.getElementById("AcVal").innerHTML;
amtVal = document.getElementById("AmVal").innerHTML;

}



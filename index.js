

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

}
 # Name to cross-reference w/ database and ensure security
from tkinter import messagebox
import pyrebase
import random

config = {
  "apiKey": "AIzaSyCs_6BTTUoPm1zZJeB5-M6PoWozSbdATnA",
  "authDomain": "atm-server-953c5.firebaseapp.com",
  "databaseURL": "https://atm-server-953c5.firebaseio.com/",
  "storageBucket": "atm-server-953c5.appspot.com"
}

firebase = pyrebase.initialize_app(config)
db = firebase.database()

class Transaction:
    def __init__(self, id, amt, action, transID):
        self.id = id
        self.amt = amt
        self.action = action
        self.balance = db.child("Bank Accounts").child(id).child("Balance").get().val()
        self.correctName = db.child("Bank Accounts").child(self.id).child("Name").get().val()
        self.transId = transID
        
        print("Initial balance:",self.balance)
        self.verify()
    def verify(self):
        if self.id in db.child("Bank Accounts").get().val():
            if self.transId in db.child("Transactions").get().val():
                if self.id == db.child("Transactions").child(self.transId).child("account").get().val():
                    if self.amt == db.child("Transactions").child(self.transId).child("amt").get().val():
                        if self.action == db.child("Transactions").child(self.transId).child("type").get().val():
                            return
                        print(db.child("Transactions").child(self.transId).child("type").get().val())
        raise ValueError("")
            #return "Invalid ID"
        

    def execute(self):
        
        try:
            self.amt = float(self.amt)
        except: 
            return "Invalid deposit type. Your deposit must be a number"
        if self.action=="deposit":
            self.deposit(self.amt)
        elif self.action=="withdraw":
            self.withdraw(self.amt)
        else:
            return "Invalid action. You must deposit or withdraw."

    def deposit(self,amt):
        self.balance = self.balance + self.amt
        db.child("Bank Accounts").child(self.id).update({"Balance": self.balance})
        print("Final balance:", self.balance)
        
    def withdraw(self,amt):
        if (self.balance > self.amt):
            self.balance = self.balance - self.amt
            db.child("Bank Accounts").child(self.id).update({"Balance": self.balance})
        else:
            return "You don't have enough money in your account to withdraw " + str(self.amt)
        print("Final balance:", self.balance)
    
# Test cases  
""" print(db.child("Bank Accounts").get().val())
db.child("Bank Accounts").child("11292906").update({"Balance": 350})

trans1 = Transaction("11292906", 400, "withdraw", "John Doe")
print(trans1.verify())

print()
# this is an invalid test case
trans2 = Transaction("999999999", 400, "withdraw", "Luke Skywalker")
print(trans2.verify())
# if block here: user clicks y/n on gui, if y, execute, else, print thank ya on the gui

print()

trans3 = Transaction("11292906", 400, "withdraw", "Mortiest Morty")
print(trans3.verify())

print()

print("Final:",db.child("Bank Accounts").get().val())
# Order: verify, then execute. """
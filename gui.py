from tkinter import *



def initWind(m):
	can = Canvas(m, width=700, height=600)
	can.pack()
	m.title('Confirm Transaction') 
	btnYes = Button(can, text='Yes', width=15, command=m.destroy, height = 1) 
	btnYes.place(x = 150, y = 550)
	btnNo = Button(can, text='No', width=15, command=m.destroy, height = 1) 
	btnNo.place(x = 450, y = 550)
	
	typeFrame = Frame(can, width = 700, height = 50)
	lblType = Label(typeFrame, width = 20, height = 2, text = "Type: ", font = ("Calibra", 20), justify = 'right')
	lblType.place(x = 0, y = 0)
	lblTypeVal = Label(typeFrame, width = 20, height = 2, text = "Withdraw/Deposit", font = ("Calibra", 20))
	lblTypeVal.place(x = 300, y = 0)
	typeFrame.place(x = 0, y = 100)

	typeFrame = Frame(can, width = 700, height = 50)
	lblType = Label(typeFrame, width = 20, height = 2, text = "Amount: ", font = ("Calibra", 20))
	lblType.place(x = 0, y = 0)
	lblTypeVal = Label(typeFrame, width = 20, height = 2, text = "$10000", font = ("Calibra", 20))
	lblTypeVal.place(x = 300, y = 0)
	typeFrame.place(x = 0, y = 200)

	typeFrame = Frame(can, width = 700, height = 50)
	lblType = Label(typeFrame, width = 20, height = 2, text = "Name: ", font = ("Calibra", 20))
	lblType.place(x = 0, y = 0)
	lblTypeVal = Label(typeFrame, width = 20, height = 2, text = "Johnny Appleseed", font = ("Calibra", 20))
	lblTypeVal.place(x = 300, y = 0)
	typeFrame.place(x = 0, y = 300)

	typeFrame = Frame(can, width = 700, height = 50)
	lblType = Label(typeFrame, width = 20, height = 2, text = "Account: ", font = ("Calibra", 20))
	lblType.place(x = 0, y = 0)
	lblTypeVal = Label(typeFrame, width = 20, height = 2, text = " XXXXXX0000", font = ("Calibra", 20))
	lblTypeVal.place(x = 300, y = 0)
	typeFrame.place(x = 0, y = 400)
	
	








m = Tk() 
initWind(m)
m.mainloop() 


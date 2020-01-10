from tkinter import *
import qr


class confirmPage(Frame):

	def __init__(self, n="", t = "", am = 0, ac = ""):
		super(confirmPage, self).__init__()
		self.name = n
		self.type = t
		self.amount = am
		self.account = ac

	def genPage(self, m, yesAction):
		def execute():
			yesAction()
			m.destroy()
		can = Canvas(m, width=700, height=600)
		can.pack()
		m.title('Confirm Transaction') 
		btnYes = Button(can, text='Yes', width=15, command=execute, height = 1) 
		btnYes.place(x = 150, y = 550)
		btnNo = Button(can, text='No', width=15, command=m.destroy, height = 1) 
		btnNo.place(x = 450, y = 550)
	
		typeFrame = Frame(can, width = 700, height = 50)
		lblType = Label(typeFrame, width = 20, height = 2, text = "Type: ", font = ("Calibra", 20), justify = 'right')
		lblType.place(x = 0, y = 0)
		lblTypeVal = Label(typeFrame, width = 20, height = 2, text = self.type, font = ("Calibra", 20))
		lblTypeVal.place(x = 300, y = 0)
		typeFrame.place(x = 0, y = 100)

		typeFrame = Frame(can, width = 700, height = 50)
		lblType = Label(typeFrame, width = 20, height = 2, text = "Amount: ", font = ("Calibra", 20))
		lblType.place(x = 0, y = 0)
		lblTypeVal = Label(typeFrame, width = 20, height = 2, text = "${}".format(self.amount), font = ("Calibra", 20))
		lblTypeVal.place(x = 300, y = 0)
		typeFrame.place(x = 0, y = 200)

		typeFrame = Frame(can, width = 700, height = 50)
		lblType = Label(typeFrame, width = 20, height = 2, text = "Name: ", font = ("Calibra", 20))
		lblType.place(x = 0, y = 0)
		lblTypeVal = Label(typeFrame, width = 20, height = 2, text = self.name, font = ("Calibra", 20))
		lblTypeVal.place(x = 300, y = 0)
		typeFrame.place(x = 0, y = 300)

		typeFrame = Frame(can, width = 700, height = 50)
		lblType = Label(typeFrame, width = 20, height = 2, text = "Account: ", font = ("Calibra", 20))
		lblType.place(x = 0, y = 0)
		lblTypeVal = Label(typeFrame, width = 20, height = 2, text = " XXXX{}".format(self.account[len(self.account)-4:]), font = ("Calibra", 20))
		lblTypeVal.place(x = 300, y = 0)
		typeFrame.place(x = 0, y = 400)








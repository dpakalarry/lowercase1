from tkinter import *

def initWind(m):
	can = Canvas(m, width=800, height=600)
	can.pack()
	m.title('Scan QR Code') 
	#button = Button(can, text='Stop', width=25, command=m.destroy) 
	#button.pack() 
	#camFrame = Frame(m, width = 200, height = 150)

	







m = Tk() 
initWind(m)
m.mainloop() 


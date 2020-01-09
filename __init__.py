import qr
import gui
import json

stream = qr.streamStart()
codeDict = None
while codeDict == None:
    codeDict = qr.readQr(stream)
    if codeDict != None:
        print("returning", codeDict)
        break
qr.quitStream(stream)
qr.cv2.destroyAllWindows()
if codeDict:
    m = gui.Tk()
    page = gui.confirmPage("Bob", codeDict["type"], codeDict["amount"], codeDict["account"])
    page.genPage(m)
    m.mainloop()
    
else:
	messagebox.showerror("ERROR", "ERROR: {}".format("Exited"))


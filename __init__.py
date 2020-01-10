import qr
import gui
import json
import backend
import webbrowser
from tkinter import messagebox
import os 
import time

stream = qr.streamStart()
codeDict = None
path = dir_path = os.getcwd()
while True:
    codeDict = qr.readQr(stream)
    if codeDict != None:
        transaction = backend.Transaction(codeDict["acct"], codeDict["amt"], codeDict["type"])
        #webbrowser.open_new_tab(path+"/confirm.html"+"?act={}&amt={}&type={}&name={}".format(
        #    codeDict["acct"], codeDict["amt"], codeDict["type"], transaction.correctName
        #))
		#TODO: Run the Node.js from here
        codeDict = None
        #time.sleep(15)
        #if sys.platform.startswith('win'):
        #    browserExe = "chrome.exe"
        #    os.system("taskkill /f /im "+browserExe)
        #elif sys.platform.startswith('linux'):
        #    browserExe = "chrome"
        #    os.system("pkill "+browserExe)


            
            
qr.quitStream(stream)
qr.cv2.destroyAllWindows()
if codeDict:
    
    m = gui.Tk()
    page = gui.confirmPage(transaction.correctName, codeDict["type"], codeDict["amount"], codeDict["account"])
    page.genPage(m, transaction.execute)
    m.mainloop()
else:
    
    messagebox.showerror("ERROR", "ERROR: {}".format("Exited"))


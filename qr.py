from imutils.video import VideoStream
from pyzbar import pyzbar
import json
import datetime
import imutils
import time
import cv2

def convertFromJson(data):
    try:
        dataDict = json.loads(data)
    except ValueError as e:
        return None
    elements = ["user", "transaction"]
    if all(elem in dataDict for elem in elements):
        return dataDict
    return None

def streamStart(pi=False):
    # initialize the video stream and allow the camera sensor to warm up
    print("[INFO] starting video stream...")
    # vs = VideoStream(src=0).start()
    vs = VideoStream(usePiCamera=pi).start()
    time.sleep(2.0)
    return vs

def readQr(stream):
    frame = stream.read()
    barcodes = pyzbar.decode(frame)
    if len(barcodes) > 0:
        barcode = barcodes.pop()
        barcodeData = barcode.data.decode("utf-8")
        barcodeType = barcode.type
        text = "{} ({})".format(barcodeData, barcodeType)
        print(text)
        return convertFromJson(barcodeData)
    return None

def quitStream(stream):
    stream.stop()

vs = streamStart()
while True:
    data = readQr(vs)
    if data != None:
        print(data)
        break
    key = cv2.waitKey(1) & 0xFF
    # if the `q` key was pressed, break from the loop
    if key == ord("q"):
        break
quitStream(vs)


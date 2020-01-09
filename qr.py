from imutils.video import VideoStream
from pyzbar import pyzbar
import argparse
import datetime
import imutils
import time
import cv2

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
    if (len(barcodes) > 0):
        for barcode in barcodes:
            barcodeData = barcode.data.decode("utf-8")
            barcodeType = barcode.type
            # draw the barcode data and barcode type on the image
            text = "{} ({})".format(barcodeData, barcodeType)
            print(text)

def quitStream(stream):
    stream.stop()

vs = streamStart()
while True:
    readQr(vs)
    key = cv2.waitKey(1) & 0xFF

    # if the `q` key was pressed, break from the loop
    if key == ord("q"):
        break
quitStream(vs)


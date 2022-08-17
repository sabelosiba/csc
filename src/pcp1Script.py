import os
import subprocess

classList = ["MeanFilterSerial", "MeanFilterParallel", "MedianFilterSerial","MedianFilterParallel" ]
windowWidth = ["3", "5", "15", "27"]

def scrpt():
    for prog in classList:
        f = open("\""+prog+ ".txt" +" \"", "x")
        f.close()
    
        for pic in range(1,5):
            f = open("\""+prog+"\"", "w")
            f.write("PICTURE -- " + str(pic) + "\n")
            f.close()
        
            for width in windowWidth:
                f = open("\""+prog+"\"", "w")
                f.write(width + "*" + width + "\n")
            
                for rep in range(1,5):
                    #f = open("\""+prog+"\"", "w")
                    os.system("java "+ prog + " pic"+str(pic)+" Outpic"+width+"*"+width+" "+ width+ " >> " + "\""+prog+".txt" +"\"")
                    f.write("\n")
                    
                f.write("\n")
                f.write("\n")
                f.close()
            
if __name__ == "__main__":
    scrpt()
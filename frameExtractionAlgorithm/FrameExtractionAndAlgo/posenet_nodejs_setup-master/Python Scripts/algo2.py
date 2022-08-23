import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import operator

array = {}
names = [
"ACPower_1_Kalidindi.csv",
"ACPower_2_Kalidindi.csv",
"ACPower_3_Kalidindi.csv",
"Algorithm_1_Kalidindi.csv",
"Algorithm_2_Kalidindi.csv",
"Algorithm_3_kalidindi.csv",
"Antenna_1_Kalidindi.csv",
"Antenna_2_Kalidindi.csv",
"Antenna_3_Kalidindi.csv",
"Authentication_1_Kalidindi.csv"
,"Authentication_2_Kalidindi.csv"
,"Authentication_3_Kalidindi.csv"
,"Authorization_1_Kalidindi.csv"
,"Authorization_2_Kalidindi.csv"
,"Authorization_3_Kalidindi.csv"
,"Bandwidth_1_Kalidindi.csv"
,"Bandwidth_2_Kalidindi.csv"
,"Bandwidth_3_Kalidindi.csv"
,"Bluetooth_1_Kalidindi.csv"
,"Bluetooth_2_Kalidindi.csv"
,"Bluetooth_3_Kalidindi.csv"
,"Browser_1_Kalidindi.csv"
,"Browser_2_Kalidindi.csv"
,"Browser_3_Kalidindi.csv"
,"CloudComputing_1_Kalidindi.csv"
,"CloudComputing_2_Kalidindi.csv"
,"CloudComputing_3_Kalidindi.csv"
,"DataCompression_1_Kalidindi.csv"
,"DataCompression_2_Kalidindi.csv"
,"DataCompression_3_Kalidindi.csv"
,"DataLinkLayer_1_Kalidindi.csv"
,"DataLinkLayer_2_Kalidindi.csv"
,"DataLinkLayer_3_Kalidindi.csv"
,"DataMining_1_Kalidindi.csv"
,"DataMining_2_Kalidindi.csv"
,"DataMining_3_Kalidindi.csv"
,"Decryption_1_Kalidindi.csv"
,"Decryption_2_Kalidindi.csv"
,"Decryption_3_Kalidindi.csv"
,"Domain_1_Kalidindi.csv"
,"Domain_2_Kalidindi.csv"
,"Domain_3_Kalidindi.csv"
,"Email_1_Kalidindi.csv"
,"Email_2_Kalidindi.csv"
,"Email_3_Kalidindi.csv"
,"Exposure_1_Kalidindi.csv"
,"Exposure_2_Kalidindi.csv"
,"Exposure_3_Kalidindi.csv"
,"Filter_1_Kalidindi.csv"
,"Filter_2_Kalidindi.csv"
,"Filter_3_Kalidindi.csv"
,"Firewall_1_Kalidindi.csv"
,"Firewall_2_Kalidindi.csv"
,"Firewall_3_Kalidindi.csv"
,"Flooding_1_Kalidindi.csv"
,"Flooding_2_Kalidindi.csv"
,"Flooding_3_Kalidindi.csv"
,"Gateway_1_Kalidindi.csv"
,"Gateway_2_Kalidindi.csv"
,"Gateway_3_Kalidindi.csv"
,"Hacker_1_Kalidindi.csv"
,"Hacker_2_Kalidindi.csv"
,"Hacker_3_Kalidindi.csv"
,"Header_1_Kalidindi.csv"
,"Header_2_Kalidindi.csv"
,"Header_3_Kalidindi.csv"
,"Hotswap_1_Kalidindi.csv"
,"Hotswap_2_Kalidindi.csv"
,"Hotswap_3_Kalidindi.csv"
,"HyperLink_1_Kalidindi.csv"
,"HyperLink_2_Kalidindi.csv"
,"HyperLink_3_Kalidindi.csv"
,"Infrastructure_1_Kalidindi.csv"
,"Infrastructure_2_Kalidindi.csv"
,"Infrastructure_3_Kalidindi.csv"
,"Integrity_1_Kalidindi.csv"
,"Integrity_2_Kalidindi.csv"
,"Integrity_3_Kalidindi.csv"
,"Internet_1_Kalidindi.csv"
,"Internet_2_Kalidindi.csv"
,"Internet_3_Kalidindi.csv"
,"Intranet_1_Kalidindi.csv"
,"Intranet_2_Kalidindi.csv"
,"Intranet_3_Kalidindi.csv"
,"Latency_1_Kalidindi.csv"
,"Latency_2_Kalidindi.csv"
,"Latency_3_Kalidindi.csv"
,"Loopback_1_Kalidindi.csv"
,"Loopback_2_Kalidindi.csv"
,"Loopback_3_Kalidindi.csv"
,"Motherboard_1_Kalidindi.csv"
,"Motherboard_2_Kalidindi.csv"
,"Motherboard_3_Kalidindi.csv"
,"Network_1_Kalidindi.csv"
,"Network_2_Kalidindi.csv"
,"Network_3_Kalidindi.csv"
,"Networking_1_Kalidindi.csv"
,"Networking_2_Kalidindi.csv"
,"Networking_3_Kalidindi.csv"
,"NetworkLayer_1_Kalidindi.csv"
,"NetworkLayer_2_Kalidindi.csv"
,"NetworkLayer_3_Kalidindi.csv"
,"Node_1_Kalidindi.csv"
,"Node_2_Kalidindi.csv"
,"Node_3_Kalidindi.csv"
,"Packet_1_Kalidindi.csv"
,"Packet_2_Kalidindi.csv"
,"Packet_3_Kalidindi.csv"
,"Partition_1_Kalidindi.csv"
,"Partition_2_Kalidindi.csv"
,"Partition_3_Kalidindi.csv"
,"PasswordSniffing_1_Kalidindi.csv"
,"PasswordSniffing_2_Kalidindi.csv"
,"PasswordSniffing_3_Kalidindi.csv"
,"Patch_1_Kalidindi.csv"
,"Patch_2_Kalidindi.csv"
,"Patch_3_Kalidindi.csv"
,"Phishing_1_Kalidindi.csv"
,"Phishing_2_Kalidindi.csv"
,"Phishing_3_Kalidindi.csv"
,"PhysicalLayer_1_Kalidindi.csv"
,"PhysicalLayer_2_Kalidindi.csv"
,"PhysicalLayer_3_Kalidindi.csv"
,"Ping_1_Kalidindi.csv"
,"Ping_2_Kalidindi.csv"
,"Ping_3_Kalidindi.csv"
,"Portscan_1_Kalidindi.csv"
,"Portscan_2_Kalidindi.csv"
,"Portscan_3_Kalidindi.csv"
,"PresentationLayer_1_Kalidindi.csv"
,"PresentationLayer_2_Kalidindi.csv"
,"PresentationLayer_3_Kalidindi.csv"
,"Protocol_1_Kalidindi.csv"
,"Protocol_2_Kalidindi.csv"
,"Protocol_3_Kalidindi.csv"
]

category = [
"acpower",
"algorithm",
"antenna",
"authentication",
"authorization"
,"bandwidth"
,"bluetooth"
,"browser"
,"cloudcomputing"
,"datacompression"
,"datalinklayer"
,"datamining"
,"decryption"
,"domain"
,"email"
,"exposure"
,"filter"
,"firewall"
,"flooding"
,"gateway"
,"hacker"
,"header"
,"hotswap"
,"hyperLink"
,"infrastructure"
,"integrity"
,"internet"
,"intranet"
,"latency"
,"loopback"
,"motherboard"
,"network"
,"networking"
,"networklayer"
,"node"
,"packet"
,"partition"
,"passwordsniffing"
,"patch"
,"phishing"
,"physicallayer"
,"ping"
,"portscan"
,"presentationlayer"
,"protocol"
]

type = {
"acpower":"Hardware",
"algorithm": "Programming",
"antenna" :"Hardware",
"authentication": "Cryptography",
"authorization" :"Cryptography",
"bandwidth" : "Networking",
"bluetooth" : "Networking",
"browser" : "Networking",
"cloudcomputing" : "Data",
"datacompression" : "Data",
"datalinklayer" : "Data",
"datamining" : "Data",
"decryption" : "Cryptography",
"domain" : "Networking",
"email" : "Networking",
"exposure" : "Cryptography",
"filter" : "Programming",
"firewall" : "Cryptography",
"flooding" : "Networking",
"gateway" : "Networking",
"hacker" : "Cryptography",
"header" : "Data",
"hotswap" : "Hardware",
"hyperlink" : "Networking",
"infrastructure" : "Hardware",
"integrity" : "Cryptography",
"internet" : "Netoworking",
"intranet" : "Netoworking",
"latency" : "Data",
"loopback" : "Networking",
"motherboard" : "Hardware",
"network" : "Networking",
"networking" : "Netoworking",
"networklayer" : "Netoworking",
"node" : "Programming",
"packet" : "Data",
"partition" : "Hardware",
"passwordsniffing" : "Cryptography",
"patch" : "Programming",
"phishing" : "Cryptography",
"physicallayer" : "Networking",
"ping" : "Networking",
"portscan" : "Networking",
"presentationlayer" : "Networking",
"protocol" : "Networking"
}


j=0
def testData(path):
    Test_CSV = path
    data = pd.read_csv(Test_CSV)

    # df = pd.DataFrame(data, columns= ['RightWrist_X'])

    # data[['RightWrist_X','RightWrist_Y']].plot()
    RW_X = data['rightWrist_x']
    RW_Y = data['rightWrist_y']

    # RW_X = data['rightWrist_x']
    # RW_Y = data['rightWrist_y']

    # plt.plot(RW_X,RW_Y)

    # Normalizing the raw data
    # NX = data['Nose_X']
    # NY = data['Nose_Y']
    NX = data['nose_x']
    NY = data['nose_y']

    # RSHX = data['RightShoulder_X']
    # LSHX = data['LeftShoulder_X']
    RSHX = data['rightShoulder_x']
    LSHX = data['leftShoulder_x']

    # HIPY = data['LeftHip_X']
    HIPY = data['leftHip_x']

    RWXN = (RW_X - NX) / (abs(LSHX - RSHX))
    RWYN = (RW_Y - NY) / (abs(NY - HIPY))

    TA = (RWXN[1:]*2 + RWYN[1:]*2)
    #_finder = path[136:]
    #removal = (finder[_finder.find(""):])
   # category = (_finder.replace(removal,""))
    newcat = type.get(category[j])


    return [TA,newcat]


def cosineSimilarity(x, y):

    # Ensure length of x and y are the same
    if len(x) != len(y) :
        return None

    # Compute the dot product between x and y
    dot_product = abs(np.dot(x, y))

    # Compute the L2 norms (magnitudes) of x and y
    magnitude_x = np.sqrt(np.sum(x**2))
    magnitude_y = np.sqrt(np.sum(y**2))

    # Compute the cosine similarity
    cosineSimilarity = dot_product / (magnitude_x * magnitude_y)

    return cosineSimilarity


#Test set 1
i=0
while(i<45):
    #print(i)
    TA = testData("C:/Users/cpotl/OneDrive/Desktop/MC/csv/"+names[i])
    TA1 = TA[0]
    TA1_Category = TA[1]
    i = i+1
    #Sample 19
    TA = testData("C:/Users/cpotl/OneDrive/Desktop/MC/csv/"+names[i])
    TA2 = TA[0]
    i = i+1
    TA = testData("C:/Users/cpotl/OneDrive/Desktop/MC/csv/"+names[i])
    TA3 = TA[0]
    i = i+1
    TA = testData("C:/Users/cpotl/OneDrive/Desktop/MC/image/data.csv")
    TA4 = TA[0]
    cos14 = cosineSimilarity(TA1,TA4)
    #print(cos14)
    cos24 = cosineSimilarity(TA2,TA4)
    #print(cos13)
    cos34 = cosineSimilarity(TA3,TA4)
    #print(cos23)
    array[j] = (cos14+cos24+cos34)/3
    j = j+1

#print(array)
sorted_d = dict( sorted(array.items(), key=operator.itemgetter(1),reverse=True))
print(sorted_d)
index = list(sorted_d.keys())[0]
print("It can be categorised as:",type[category[index]])
print("Accuracy is:",array[index])
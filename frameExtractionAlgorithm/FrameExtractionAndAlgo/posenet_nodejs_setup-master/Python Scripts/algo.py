import pandas as pd
import numpy as np
import matplotlib.pyplot as plt1
import matplotlib.pyplot as plt
from numpy import dot
import math
from numpy.linalg import norm

from dtaidistance import dtw
from dtaidistance import dtw_visualisation as dtwvis


def cosine_similarity(x, y):

    # Ensure length of x and y are the same
    if len(x) != len(y) :
        return None

    # Compute the dot product between x and y
    dot_product = np.dot(x, y)

    # Compute the L2 norms (magnitudes) of x and y
    magnitude_x = np.sqrt(np.sum(x**2))
    magnitude_y = np.sqrt(np.sum(y**2))

    # Compute the cosine similarity
    cosine_similarity = dot_product / (magnitude_x * magnitude_y)

    return cosine_similarity

#Sample 1
Test_CSV = "C:/Users/cpotl/OneDrive/Desktop/MC/csv/ACPower_1_Kalidindi.csv"
data = pd.read_csv (Test_CSV)
#df = pd.DataFrame(data, columns= ['RightWrist_X'])

#data[['RightWrist_X','RightWrist_Y']].plot()
RW_X = data['rightWrist_x']
RW_Y = data['rightWrist_y']

# RW_X = data['rightWrist_x']
# RW_Y = data['rightWrist_y']

# plt.plot(RW_X,RW_Y)

# Normalizing the raw data
#NX = data['Nose_X']
#NY = data['Nose_Y']
NX = data['nose_x']
NY = data['nose_y']

#RSHX = data['RightShoulder_X']
#LSHX = data['LeftShoulder_X']
RSHX = data['rightShoulder_x']
LSHX = data['leftShoulder_x']

#HIPY = data['LeftHip_X']
HIPY = data['leftHip_x']


RWXN = (RW_X - NX) / (abs(LSHX - RSHX))
RWYN = (RW_Y - NY) / (abs(NY - HIPY))




#Sample 2
Test_CSV_1 = "C:/Users/cpotl/OneDrive/Desktop/MC/csv/ACPower_2_Kalidindi.csv"
data1 = pd.read_csv (Test_CSV_1)
#df = pd.DataFrame(data, columns= ['RightWrist_X'])

#data[['RightWrist_X','RightWrist_Y']].plot()
#RW_X1 = data1['RightWrist_X']
#RW_Y1 = data1['RightWrist_Y']
RW_X1 = data1['rightWrist_x']
RW_Y1 = data1['rightWrist_y']

# plt.plot(RW_X,RW_Y)

# Normalizing the raw data
#NX1 = data1['Nose_X']
#NY1 = data1['Nose_Y']
NX1 = data1['nose_x']
NY1 = data1['nose_y']

#RSHX1 = data1['RightShoulder_X']
#LSHX1 = data1['LeftShoulder_X']
RSHX1 = data1['rightShoulder_x']
LSHX1 = data1['leftShoulder_x']

#HIPY1 = data1['LeftHip_X']
HIPY1 = data1['leftHip_x']


RWXN1 = (RW_X1 - NX1) / (abs(LSHX1 - RSHX1))
RWYN1 = (RW_Y1 - NY1) / (abs(NY1 - HIPY1))



# Trajectory
TA = (RWXN[10:]*2 + RWYN[10:]*2)
TA1 = RWXN1[10:]*2 + RWYN1[10:]*2

cos_sim = dot(TA, TA1)/(norm(TA)*norm(TA1))
print(cos_sim)

#print(cosine_similarity(TA,TA1))

#dist = dtw.distance(TA,TA1)


plt.plot(RWXN[10:],RWYN[10:])
plt.show()



plt1.plot(RWXN1[10:],RWYN1[10:])
plt1.show()
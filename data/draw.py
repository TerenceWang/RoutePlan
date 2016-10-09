import numpy as np
import matplotlib.pyplot as plt
import fileinput
filename = "500-result.txt"
dstartime=[]
dstarlength=[]
dijtime=[]
dijlength=[]
for line in fileinput.input(filename):
    s=[]
    s=line.split(" ")
    dstartime.append(s[0])
    dstarlength.append(s[1])
    dijtime.append(s[2])
    dijlength.append(s[3])

def count(list):

    result=[0 for i in range(0,31)]
    for i in list:
        id = (int)(i)/5
        if id >= 30:
            id = 30
        result[id]=result[id]+1
    return result

dijlen=count(dijlength)
dijt = count(dijtime)

dstart=count(dstartime)
dstarlen=count(dstarlength)

n_groups = 31
print [i*5 for i in range(31)]
means_men = (20, 35, 30, 35, 27)
means_women = (25, 32, 34, 20, 25)
fig, ax = plt.subplots()
index = np.arange(n_groups)
bar_width = 0.2
opacity = 0.8
rects1 = plt.bar(index, dijt, bar_width, alpha=opacity, color="#4DB6AC", label='DijTime')
rects3 = plt.bar(index + 1 * bar_width, dstart, bar_width, alpha=opacity, color="#004D40", label='DStarTime')

rects2 = plt.bar(index + 2 * bar_width, dijlen, bar_width, alpha=opacity, color = "#FF8A65", label = 'DijLength')
rects4 = plt.bar(index + 3 * bar_width, dstarlen, bar_width, alpha=opacity, color='#BF360C', label='DStarLength')

plt.xlabel('Distribute')
plt.ylabel('Number')
plt.title('Compare')
plt.xticks(index + bar_width, [i*5 for i in range(31)])
plt.ylim(0, 300)
plt.legend()
plt.tight_layout()
plt.show()


# plt.figure(1)
# plt.figure(2)
# ax1 = plt.subplot(211)
# ax2 = plt.subplot(212)
#
# x = np.linspace(0, 3, 100)
# for i in xrange(5):
#     plt.figure(1)
#     plt.plot(x, np.exp(i * x / 3))
#     plt.sca(ax1)
#     plt.plot(x, np.sin(i * x))
#     plt.sca(ax2)
#     plt.plot(x, np.cos(i * x))
#
# plt.show()
import random
nodenumber_line=10  # node per line
nodenumber_total=nodenumber_line*nodenumber_line
edge_weight=10      # max weight of a single edge
edgenumber=nodenumber_line*(nodenumber_line-1)*2*2
singleratio=0.2     # ratio of the single road
edge=[[-1 for i in range(nodenumber_total)]for j in range(nodenumber_total)]
for i in range(nodenumber_total):
    row=i/nodenumber_line
    col=i%nodenumber_line
    if row != nodenumber_line-1 and col != nodenumber_line-1:
        edge[i][i+1]=random.randint(1,edge_weight)
        edge[i+1][i]=edge[i][i+1]
        edge[i][i+nodenumber_line]=random.randint(1,edge_weight)
        edge[i+nodenumber_line][i]=edge[i][i+nodenumber_line]
    elif row == nodenumber_line-1 and col != nodenumber_line-1:
        edge[i][i+1]=random.randint(1,edge_weight)
        edge[i+1][i]=edge[i][i+1]
    elif row != nodenumber_line-1 and col == nodenumber_line-1:
        edge[i][i+nodenumber_line]=random.randint(1,edge_weight)
        edge[i+nodenumber_line][i]=edge[i][i+nodenumber_line]
list = []
for i in range(nodenumber_total):
    for j in range(nodenumber_total):
        if edge[i][j]>0 and i < j:
            list.append([i,j,edge[i][j]])
            list.append([j,i,edge[j][i]])
singlenumber=int(edgenumber/2*singleratio)
singleid=[random.randint(0,len(list)/2-1) for _ in range(singlenumber)]
singleid=set(singleid)
tmp=[]
for i in singleid:
    tmp.append(list[i*2+random.randint(0,1)])
for i in tmp:
    list.remove(i)
filename="nodenumber_"+str(nodenumber_line)+"_edgenumber_"+str(len(list))+".map"
out = open(filename,"w")
out.write(str(nodenumber_line)+" "+str(nodenumber_total)+" "+str(len(list))+"\n")
for i in list:
    out.write(str(i[0])+" "+str(i[1])+" "+str(i[2])+"\n")
out.close()
a=[random.randint(0,10) for _ in range(10)]

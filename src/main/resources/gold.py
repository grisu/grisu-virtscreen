import sys

p_hash = {}
total = 0

for i in sys.stdin.readlines():
    try:
        total +=1
#        p_hash[i[0:-1] + ".local"] += 1
        p_hash[i[0:-1]] += 1
    except:
#        p_hash[i[0:-1] + ".local"] = 1
        p_hash[i[0:-1]] = 1
    pass

p_list = []

for (k,v) in p_hash.items():
    print k," ",v
print "no_of_processes ",total

# A lambda function to interact with AWS RDS MySQL

import pymysql
import sys
import json

REGION = 'us-east-1a'
rds_host  = "database-1.cmxlclti9vqy.us-east-1.rds.amazonaws.com"
name = "admin"
password = "learnforfun1!"
db_name = "wanderfo"

#connect to the database
conn = pymysql.connect(rds_host, user=name, passwd=password, db=db_name, connect_timeout=5)

def save_events(event):
    """
    This function fetches content from mysql RDS instance
    """
    result = []
    conn = pymysql.connect(rds_host, user=name, passwd=password, db=db_name, connect_timeout=5)
    with conn.cursor() as cur:
        #cur.execute("""insert into test (id, name) values( %s, '%s')""" % (event['id'], event['name']))
        cur.execute("""select * from test""")
        conn.commit()
        cur.close()
       # for row in cur:
        #    result.append(list(row))
        #print "Data from RDS..."
        #print result
        
        
        
#update shop bio
def update_bio(shopname,value):
    with conn.cursor() as cur:
        sql = "update shop_deets set bio=%s where shop_name=%s"
        cur.execute(sql, (value, shopname)) 
        conn.commit()
        cur.close()
        return "success"
        

#set/ delete shop items
def update_items(event, name, type, typeValue, shopname,action):
    with conn.cursor() as cur:
        dict={}
        sql0 = "select num_items, item_name0, item_name1, item_name2, item_name3, item_name4, item_name5 from shop_deets where shop_name=%s"
        cur.execute(sql0, shopname)
        row = cur.fetchone();
        print(row)
        dict["0"] = row[1]
        dict["1"] = row[2]
        dict["2"] = row[3]
        dict["3"] = row[4]
        dict["4"] = row[5]
        dict["5"] = row[6]
        print(dict)
        #set item
        if(action=="setitem"):
            num = str(int(row[0])+1)
            count = str(int(num)-1)
            for item in row:
                if (item == name):
                    return "sameItemNameError"
                else:
                    continue
            item_name = "item_name" + count
            item_type = "item_type" + count
            item_type_val = "item_type_val" + count
            sql = "update shop_deets set num_items=%s, " +item_name+"=%s, "+item_type+"=%s, "+item_type_val+"=%s where shop_name=%s"
            cur.execute(sql, (num, name, type, typeValue, shopname)) 
            conn.commit()
            cur.close()
            return "success"
        
        #delete item
        if(action=="deleteitem"):
            count = -1;
            for item in row:
                num = str(int(row[0])-1)
                if (item == name):
                    item_name = "item_name" + str(count)
                    item_type = "item_type" + str(count)
                    item_type_val = "item_type_val" + str(count)
                    sql = "update shop_deets set num_items=%s, " +item_name+"=%s, "+item_type+"=%s, "+item_type_val+"=%s where shop_name=%s"
                    cur.execute(sql, (num, "NULL", "NULL", "NULL", shopname)) 
                    conn.commit()
                    cur.close()
                    return "success"
                elif (count<7):
                    count += 1
                else:
                    return "noSuchEntryError"
                  
                    
def set_permission(shopname, permission):
    with conn.cursor() as cur:
        sql = "update shop_deets set permission=%s where shop_name=%s"
        cur.execute(sql, (permission, shopname)) 
        conn.commit()
        cur.close()
        return "success"
        

def main(event, context):
    status = ""
    group = event.get("group")
    target = group['target']
    shopname = group['shopname']
    value = group['value']
    
    if target=="deleteitem":
        print("deleteitems")
        # value is the (itemname,type,typeValue) form
        name,type,typeValue = value.split(",")
        status = update_items(group, name, type, typeValue, shopname,"deleteitem")
        print("status: "+status)
        #return status
    
    elif target=="setbio":
        print("setbio")
        # value is the new bio here
        status = update_bio(shopname,value)
        print("status: "+status)
        #return status
    
    elif target=="setitem":
        print("setitem")
        # value is the (itemname,type,typeValue) form
        name,type,typeValue = value.split(",")
        status = update_items(group, name, type, typeValue, shopname,"setitem")
        print("status: "+status)
        #return status
        
    elif target=="edititem":
        print("edititem")
         # value is the (itemname,type,typeValue) form
        name,type,typeValue = value.split(",")
        # delete first
        status1 = update_items(group, name, type, typeValue, shopname,"deleteitem")
        # then add item
        status2 = update_items(group, name, type, typeValue, shopname,"setitem")
        status = "success" 
        
    elif target=="setpermission":
        print("setpermission")
        # value is the permission
        status = set_permission(shopname, value)
        print("status: "+status)
        #return status

    else:
        # bad "target" given
        #return "fail"
        status = "fail"
        
    
    # a Python object (dict):
    x = {
      "status":status
    }

    return x

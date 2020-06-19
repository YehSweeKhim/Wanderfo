# A lambda function to interact with AWS RDS MySQL

import pymysql
import sys

REGION = 'us-east-1a'
rds_host  = "database-1.cmxlclti9vqy.us-east-1.rds.amazonaws.com"
name = "admin"
password = "learnforfun1!"
db_name = "wanderfo"

#connect to the database
conn = pymysql.connect(rds_host, user=name, passwd=password, db=db_name, connect_timeout=5)
        
#get shops that are within gray areas        
def get_gray(new_cat):
    #get old category status
    old_cat = get_status()
    dict={}
    dict_data={}
    dict_one={}
    dict_multi={}
    dict_multifiltered={}
    dict_status={}
    shops=[]
    list_dict=[]
    list_affected=[]
    grayshop=""
    with conn.cursor() as cur:
        columns_sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'business' "
        cur.execute(columns_sql)
        row = cur.fetchall()
        print(row)
        col = list(row)
        col = col[3::]
        print(col)
        for i in range(len(col)):
            col1 = col[i][0]
            dict[i] = col1
            list_dict.append(col1)
        print(dict)
        # get which index/category numbers needs to be changed
        for i in range(len(old_cat)):
            if old_cat[i]!=new_cat[i]:
                list_affected.append(dict.get(i))
        print(list_affected)
        
        # dict of shops with one category
        shops_sql = "select shop_name from business"
        cur.execute(shops_sql)
        row1 = cur.fetchall()
        print(row1)
        shop = list(row1)
        shop = shop[1::]
        print(shop)
        for i in range(len(shop)):
            shop1 = shop[i][0]
            shops.append(shop1)
        print(shops)
        for shop in shops:
            #getrow for each shop and append it into dict_data
            sql="select * from business where shop_name=%s"
            cur.execute(sql,shop)
            shop_row = cur.fetchone()
            shop_row = shop_row[3::]
            print(shop_row)
            list_cat = []
            #check if the row has 1
            for i in range(len(shop_row)):
                if (shop_row[i] == 1):
                    dict_data[shop] = list_cat
                    dict_data[shop].append(dict.get(i))
            print(dict_data)
            #split the dict btwn onecategory and multicategory
            for key, val in dict_data.items():
                if (len(val)>1):
                    dict_multi[key] = val
                elif(len(val)==1):
                    dict_one[key] = val
            print(dict_multi)
            print(dict_one)
        
        #dict of shops with one category (looping through each shop)
        for key,val in dict_one.items():
            print(list_affected)
            if(val[0] in list_affected):
                #get index of category to retrieve the permission from net_cat
                print(dict)
                print(new_cat)
                for index, cat in dict.items():
                    if (cat == val[0]):
                        permission = new_cat[index]
                        print(permission)
                        #set permission for that shop
                        set_permission(key, permission)
                        print("success")
        
        #dict of shops with multi category (filter to get shops with affected cat)
        for key, val in dict_multi.items():
            for i in list_affected:
                if (i in val):
                    dict_multifiltered[key] = val
        print(list_affected)
        print(dict_multi)
        print(dict_multifiltered)
        
        print(dict)
        #loop through dict_multifiltered
        for key, val in dict_multifiltered.items():
            #get index of category using dict
            for id in val:
                list_status=[]
                for index, cat in dict.items():
                    if(id == cat):
                        if key in dict_status:
                            dict_status[key].append(index)
                        else:
                            dict_status[key] = list_status
                            dict_status[key].append(index)
        
        print(new_cat)
        print(dict_status)
        #{'fairprice': [0, 1, 8, 12]}
        for key,val in dict_status.items():
            print("new_dict")
        #using index, if it matches i in new_cat, that is the new category status
        #check each val with new_cat(string)
            new_status=""
            for id in val:
                # print(id)
                # print(range(len(new_cat)))
                for i in range(len(new_cat)):
                    if(id==i):
                        new_status+=new_cat[i]
                check0=0
                check1=0
                length = len(new_status)
                for j in new_status:
                    if(j=="0"):
                        check0+=1
                    elif(j=="1"):
                        check1+=1
            print(new_status)
            print(str(check0)+"check0")
            print(str(check1)+"check1")
            #close shop
            if(check0==length):
                set_permission(key, "0")
                print(key+"set to 0")
            #open shop
            elif(check1==length):
                set_permission(key, "1")
                print(key+"set to 1")
            else:
                get_permission = "select permission from business where shop_name=%s"
                cur.execute(get_permission,key)
                perm=cur.fetchone()
                perm = perm[0]
                print("permission"+str(perm))
                grayshop+=key+","+str(perm)+","
        
        #update the status with new_cat
        for index in range(len(list_dict)):
            print(list_dict[index])
            print(new_cat[index])
            update_status = "update business set "+list_dict[index]+"=%s where shop_name='status'"
            cur.execute(update_status,new_cat[index])
            conn.commit()
        
        print(grayshop.strip(","))
        return grayshop.strip(",")
                
            
#get status of each category
def get_status():
    status=""
    with conn.cursor() as cur:
        sql="select * from business where shop_name='status'"
        cur.execute(sql)
        row = cur.fetchone()
        listt = list(row)
        itemlist = listt[3:]
        for category in itemlist:
            status+=str(category)
        conn.commit()
        cur.close()
        return status
      
        
#get shop of category
def get_shop(category):
    str=""
    with conn.cursor() as cur:
        sql="select shop_name from business where "+category+" ='1'"
        cur.execute(sql)
        rows = cur.fetchall()
        for row in rows:
            str+=row[0] + ","
        conn.commit()
        cur.close()
        return str.strip(",")
        
        
#set permission for shops
def set_permission(shopname, permission):
    with conn.cursor() as cur:
        sql = "update business set permission=%s where shop_name=%s"
        cur.execute(sql,(permission,shopname)) 
        conn.commit()
        cur.close()
        return "success"
        
        
def main(event, context):
    target = event['target']
    shopname = event['shopname']
    value = event['value']
    
    if target=="setpermission":
        print("setpermission")
        # value is the permission
        status = set_permission(shopname,value)
        print("status: "+status)
        return status
        
    elif target=="getstatus":
        print("getstatus")
        # value is the permission
        status = get_status()
        return status
        
    elif target=="getgray":
        print("getgray")
        grayshops = get_gray(value)
        return grayshops

    else:
        # bad "target" given
        return "fail"
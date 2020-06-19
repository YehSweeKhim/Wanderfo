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

#helper functions
def chunks(lst, n):
    final = []
    for i in range(0, len(lst), n):
        elem = lst[i:i + n]
        final.append(elem)
    return final
    
#get permission
def get_permission(shopname):
    with conn.cursor() as cur:
        sql = "select permission from business where shop_name=%s"
        cur.execute(sql, shopname) 
        row = cur.fetchone()
        conn.commit()
        cur.close()
        return row[0]
        
#get status
def get_status(shopname):
    with conn.cursor() as cur:
        sql = "select status from shop_deets where shop_name=%s"
        cur.execute(sql, shopname) 
        row = cur.fetchone()
        conn.commit()
        cur.close()
        return row[0]
        
#get shop bio
def get_bio(shopname):
    with conn.cursor() as cur:
        sql = "select bio from shop_deets where shop_name=%s"
        cur.execute(sql, shopname) 
        row = cur.fetchone()
        conn.commit()
        cur.close()
        if(row[0] == None or row[0] == "NU"):
            return ""
        else:
            return row[0]
        
#get all shop details
def get_item(shopname):
    with conn.cursor() as cur:
        sql="select * from shop_deets where shop_name=%s"
        cur.execute(sql, shopname)
        row = cur.fetchone()
        conn.commit()
        cur.close()
        print(row)
        listt = list(row)
        print(listt)
        itemlist = listt[5:]
        print("itemlist ",itemlist)
        itemNum = listt[4]
        mitemlist = chunks(itemlist,3)
        final = ""
        itemcount = 0
        for i in range(len(mitemlist)):
            #there is an item
            if mitemlist[i][0]!=None and mitemlist[i][0]!="NULL":
                itemcount+=1
                final=final+mitemlist[i][0]+","+mitemlist[i][1]+","+mitemlist[i][2]+","
            if itemcount==itemNum:
                break
        return final.strip(",")
        
#get shop of category
def get_shop(category):
    str=""
    with conn.cursor() as cur:
        sql="select shop_name from business where "+category+" ='1'"
        cur.execute(sql)
        rows = cur.fetchall()
        for row in rows:
            str+=row[1] + ","
        conn.commit()
        cur.close()
        return str.strip(",")
        
        
#get category of shop
def get_category(shopname):
    dict={}
    str=""
    with conn.cursor() as cur:
        sql="select * from business where shop_name=%s"
        cur.execute(sql, shopname)
        row = cur.fetchone()
        dict["food_supply"]=row[2]
        dict["food_manufacturing"]=row[3]
        dict["fnd_outlets"]=row[4]
        dict["food_caterers"]=row[5]
        dict["food_delivery_services"]=row[6]
        dict["food_packaging"]=row[7]
        dict["laboratory_food_safety"]=row[8]
        dict["shelf_life"]=row[9]
        dict["manufacturers_of_essential_products"]=row[10]
        dict["semi_conductor"]=row[11]
        dict["aerospace"]=row[12]
        dict["manufacturing_of_print"]=row[13]
        dict["distributor_of_essential_product"]=row[14]
        for key, val in dict.items():
            if (val == 1):
                str+= key + ","
        conn.commit()
        cur.close() 
        return str.strip(",")
        
def get_shopname():
    shopname=""
    with conn.cursor() as cur:
        sql="select shop_name from shop_deets"
        cur.execute(sql)
        rows = cur.fetchall()
        for row in rows:
            shopname+=row[0] + ","
        conn.commit()
        cur.close()
        return shopname
    
 
def main(event, context):
    target = event['target']
    key = event['key']
    
    if target=="getcategory":
        print("getcategory")
        #key here is the shopname
        category = get_category(key)
        print(category)
        return category
    
    elif target=="getbio":
        print("getbio")
        # key here is the shopname
        bio = get_bio(key)
        print(bio)
        return bio
        
    elif target=="getitem":
        print("getitem")
        # key here is the shopname
        item = get_item(key)
        print(item)
        if item == "":
            item = "nothing,-,-"
        return item
        
    elif target=="getpermission":
        print("getpermission")
        # key here is the shopname
        permission = get_permission(key)
        print(permission)
        return permission
        
    elif target=="getstatus":
        print("getstatus")
        # key here is the shopname
        status = get_status(key)
        print(status)
        return status
        
    elif target=="getshopdetail":
        print("getshopdetail")
        permission = get_permission(key)
        status = get_status(key)
        category = get_category(key)
        bio = get_bio(key)
        item = get_item(key)
        if item == "":
            item = "nothing,-,-"
        shopdetail = "permission:"+str(permission)+", "+"status:"+str(status)+", "+"category:"+category+", "+"bio:"+bio+", "+"item:"+item
        print(shopdetail)
        return shopdetail
        
    elif target=="getshop":
        print("getshop")
        # key here is the category
        list = get_shop(key)
        print(list)
        return list
        
    elif target=="getshopname":
        print("getshopname")
        # key here is the category
        shopname = get_shopname()
        print(shopname)
        return shopname
        
    else:
        # bad "target" given
        return "fail"
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
        
#set permission for shops
def set_permission(shopname, permission):
    with conn.cursor() as cur:
        sql = "update business set permission=%s where shop_name=%s"
        cur.execute(sql,(permission,shopname)) 
        conn.commit()
        cur.close()
        return "success"
        
def main(event, context):
    group = event.get('group')
    target = group['target']
    shopname = group['shopname']
    value = group['value']
    
    if target=="setpermission":
        print("setpermission")
        # value is the permission
        status = set_permission(shopname,value)
        print("status: "+status)
        x = {"status":status}
        return x

    else:
        # bad "target" given
        x = {"status":"fail"}
        return x
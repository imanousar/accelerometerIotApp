import paho.mqtt.client as mqtt

def on_connect(client , userdata , flags,rc):
    print("connected :" +str(rc))
    client.subscribe("Tutorial/")
    
    
def on_message(client, userdata ,msg):
    print (str(msg.payload))
    
client = mqtt.Client()
client.on_connect= on_connect
client.on_message = on_message
client.connect("chimpanzee.rmq.cloudamqp.com",1883   ,60)
client.username_pw_set("uglyketc:uglyketc","jRCfZsIvJiM0-UBkrW0IubTPwPAu4K6B")



client.loop_forever()

#/bin/bash

args=("$@")

eventId=${args[0]}
#this user(objectId from collection SpochtUser) created the event in app
origUserId=${args[1]}
#this user wants to join the game
otherUserId=${args[2]}
#the outcome
outcome=${args[3]}

echo $eventId;
echo "$eventId";

#echo "CheckoutOrigUser"
#retCheckoutOrigUser=`curl -X POST  -H "X-Parse-Application-Id: IvP2CsQV7fRqfg0tSQs2Ugot9YCDo4VAdRUYsQFd"  -H "X-Parse-REST-API-Key: MfTV1x7f62HBQp0CMHj64dFpQBWZ3EcNpB2FnBtJ"  -H "Content-Type: application/json"  -d '{"event":{"id" : "'$eventId'"}, "user":{"id" : "'$origUserId'"}, "outcome" : {"value": "'$outcome'"}}' https://api.parse.com/1/functions/checkout`
#echo $retCheckoutOrigUser
#sleep 5


echo "CheckinOtherUser"
retCheckinOtherUser=`curl -X POST  -H "X-Parse-Application-Id: IvP2CsQV7fRqfg0tSQs2Ugot9YCDo4VAdRUYsQFd"  -H "X-Parse-REST-API-Key: MfTV1x7f62HBQp0CMHj64dFpQBWZ3EcNpB2FnBtJ"  -H "Content-Type: application/json"  -d '{"event":{"id" : "'$eventId'"}, "user":{"id" : "'$otherUserId'"}, "outcome" : {"value": "'$outcome'"}}'  https://api.parse.com/1/functions/checkin`
echo $retCheckinOtherUser
sleep 5


echo "CheckoutOtherUser"
retCheckoutOrigUser=`curl -X POST  -H "X-Parse-Application-Id: IvP2CsQV7fRqfg0tSQs2Ugot9YCDo4VAdRUYsQFd"  -H "X-Parse-REST-API-Key: MfTV1x7f62HBQp0CMHj64dFpQBWZ3EcNpB2FnBtJ"  -H "Content-Type: application/json"  -d '{"event":{"id" : "'$eventId'"}, "user":{"id" : "'$otherUserId'"}, "outcome" : {"value": "'$outcome'"}}'  https://api.parse.com/1/functions/checkout`
echo $retCheckoutOtherUser
sleep 5


echo "CheckinOtherUserAgain"
retCheckinOtherUserAgain=`curl -X POST  -H "X-Parse-Application-Id: IvP2CsQV7fRqfg0tSQs2Ugot9YCDo4VAdRUYsQFd"  -H "X-Parse-REST-API-Key: MfTV1x7f62HBQp0CMHj64dFpQBWZ3EcNpB2FnBtJ"  -H "Content-Type: application/json"  -d '{"event":{"id" : "'$eventId'"}, "user":{"id" : "'$otherUserId'"}, "outcome" : {"value": "'$outcome'"}}'  https://api.parse.com/1/functions/checkin`
echo $retCheckinOtherUserAgain
sleep 5


echo "StartGame"
retStartGame=`curl -X POST  -H "X-Parse-Application-Id: IvP2CsQV7fRqfg0tSQs2Ugot9YCDo4VAdRUYsQFd"  -H "X-Parse-REST-API-Key: MfTV1x7f62HBQp0CMHj64dFpQBWZ3EcNpB2FnBtJ"  -H "Content-Type: application/json"  -d '{"event":{"id" : "'$eventId'"}, "user":{"id" : "'$otherUserId'"}, "outcome" : {"value": "'$outcome'"}}'  https://api.parse.com/1/functions/startGame`
echo $retStartGame
sleep 5


echo "StopGame"
retStopGame=`curl -X POST  -H "X-Parse-Application-Id: IvP2CsQV7fRqfg0tSQs2Ugot9YCDo4VAdRUYsQFd"  -H "X-Parse-REST-API-Key: MfTV1x7f62HBQp0CMHj64dFpQBWZ3EcNpB2FnBtJ"  -H "Content-Type: application/json"  -d '{"event":{"id" : "'$eventId'"}, "user":{"id" : "'$otherUserId'"}, "outcome" : {"value": "'$outcome'"}}'  https://api.parse.com/1/functions/stopGame`
echo $retStopGame
sleep 5


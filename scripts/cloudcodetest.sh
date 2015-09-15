#!/bin/bash
  ret=`curl -X POST  -H "X-Parse-Application-Id: IvP2CsQV7fRqfg0tSQs2Ugot9YCDo4VAdRUYsQFd"  -H "X-Parse-REST-API-Key: MfTV1x7f62HBQp0CMHj64dFpQBWZ3EcNpB2FnBtJ"  -H "Content-Type: application/json"  -d '{"event":{"id" : "VRjWyksupm"}, "user":{"id" : "O0RRqBaAIp"}}'  https://api.parse.com/1/functions/stopGame`
  echo $ret
  sleep 2



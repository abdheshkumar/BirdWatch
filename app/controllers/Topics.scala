package controllers

import play.api.mvc.{Action, Controller}

import utilities.Conf
import actors.TwitterClient
import actors.TwitterClient.{Start, RemoveTopic, AddTopic}

object Topics extends Controller {

  val accessToken = Conf.getOrEmpty("admin.accessToken")

  /** Serves HTML page (static content at the moment, page gets updates through WebSocket) */
  def add(topic: String, token: String) = Action {
    implicit req => {
      if (token == accessToken) {        
        TwitterClient.tweetClientSupervisor ! AddTopic(topic)
        TwitterClient.tweetClientSupervisor ! Start
        Ok("Topic added: " + topic + "\nToken: " + token)
      }
      else Unauthorized("You are not authorized to add topic " + topic)
    }
  }

  /** Serves HTML page (static content at the moment, page gets updates through WebSocket) */
  def remove(topic: String, token: String) = Action {
    implicit req => {
      if (token == accessToken) {
        TwitterClient.tweetClientSupervisor ! RemoveTopic(topic)
        TwitterClient.tweetClientSupervisor ! Start
        Ok("Topic removed: " + topic + "\nToken: " + token)
      }
      else Unauthorized("You are not authorized to add topic " + topic)
    }
  }
}

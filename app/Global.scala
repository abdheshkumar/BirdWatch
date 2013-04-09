import models.Tweet
import play.api.GlobalSettings
import utils.Mongo

object Global extends GlobalSettings {

  override def onStart(application: play.api.Application) {
    Tweet.listen("obama")   // multiple search words are separated by "%2C"
  }  
  
  override def onStop(application: play.api.Application) {
    Mongo.connection.close()
  }
}

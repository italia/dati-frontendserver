package services.push_notification

import play.api.{Configuration, Environment}
import ftd_api.yaml.{Error, Subscription, Success}
import repositories.push_notification.{PushNotificationRepository, PushNotificationRepositoryComponent}

import scala.concurrent.Future

trait PushNotificationServiceComponent {
  this: PushNotificationRepositoryComponent =>
  val pushNotificationService: PushNotificationService

  class PushNotificationService {
    def save(user:String, subscription: Subscription): Future[Either[Error, Success]] = {
      pushNotificationRepository.save(user, subscription)
    }

    def getSubscriptions(user: String): Future[Seq[Subscription]] = {
      pushNotificationRepository.getSubscriptions(user)
    }
  }
}

object PushNotificationRegistry extends PushNotificationRepositoryComponent with PushNotificationServiceComponent{

  val conf = Configuration.load(Environment.simple())
  val app: String = conf.getString("app.type").getOrElse("prod")
  val pushNotificationRepository: PushNotificationRepository =  PushNotificationRepository(app)
  val pushNotificationService = new PushNotificationService
}
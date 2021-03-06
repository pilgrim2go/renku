package ch.renku.acceptancetests.pages

import ch.renku.acceptancetests.model.projects.ProjectDetails
import ch.renku.acceptancetests.pages.Page.{Path, Title}
import ch.renku.acceptancetests.tooling.ScreenCapturing
import eu.timepit.refined.auto._
import org.openqa.selenium.{WebDriver, WebElement}
import org.scalatestplus.selenium.{Driver, WebBrowser}
import org.scalatestplus.selenium.WebBrowser.{cssSelector, find}

import scala.concurrent.duration._
import scala.language.postfixOps

case object NewProjectPage extends RenkuPage with TopBar with ScreenCapturing {

  override val path:  Path  = "/project_new"
  override val title: Title = "Renku"

  override def pageReadyElement(implicit webDriver: WebDriver): Option[WebElement] = Some(createButton)

  def submitFormWith(
      project:          ProjectDetails
  )(implicit webDriver: WebDriver, browser: WebBrowser with Driver, captureScreenshots: Boolean = false): Unit =
    eventually {
      titleField.clear() sleep (1 second)
      titleField.enterValue(project.title.value) sleep (1 second)

      descriptionField.clear() sleep (1 second)
      descriptionField.enterValue(project.description.value) sleep (1 second)

      if (captureScreenshots) saveScreenshot

      createButton.click() sleep (2 seconds)
    }

  private def titleField(implicit webDriver: WebDriver): WebElement = eventually {
    find(cssSelector("input#title")) getOrElse fail("Title field not found")
  }

  private def descriptionField(implicit webDriver: WebDriver): WebElement = eventually {
    find(cssSelector("textarea#description")) getOrElse fail("Description field not found")
  }

  def createButton(implicit webDriver: WebDriver): WebElement = eventually {
    find(cssSelector("#create-new-project")) getOrElse fail("Create button not found")
  }
}

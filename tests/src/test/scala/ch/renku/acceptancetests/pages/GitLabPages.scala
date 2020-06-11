package ch.renku.acceptancetests.pages

import ch.renku.acceptancetests.model.projects.ProjectDetails
import ch.renku.acceptancetests.model.projects.ProjectDetails._
import ch.renku.acceptancetests.model.users.UserCredentials
import ch.renku.acceptancetests.pages.Page.{Path, Title}
import ch.renku.acceptancetests.tooling.BaseUrl
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.string.Url
import org.openqa.selenium.{WebDriver, WebElement}
import org.scalatestplus.selenium.WebBrowser.{cssSelector, find}
import org.scalatestplus.selenium.{Driver, WebBrowser}

import scala.concurrent.duration._
import scala.language.postfixOps

object GitLabPages {

  def apply()(implicit projectDetails: ProjectDetails, userCredentials: UserCredentials): GitLabPages =
    new GitLabPages(projectDetails, userCredentials)

  case class GitLabBaseUrl(value: String Refined Url) extends BaseUrl(value)

  sealed abstract class GitLabPage extends Page[GitLabBaseUrl]
}

class GitLabPages(
    projectDetails:  ProjectDetails,
    userCredentials: UserCredentials
) {

  import GitLabPages._

  case object ProjectPage extends GitLabPage {

    override val path: Path = Refined.unsafeApply(
      s"/gitlab/${userCredentials.userNamespace}/${projectDetails.title.toPathSegment}"
    )

    // val branchesPath: String =
    //   s"/gitlab/${userCredentials.userNamespace}/${projectDetails.title.toPathSegment}/-/branches"

    override val title: Title = Refined.unsafeApply(
      s"${userCredentials.fullName} / ${projectDetails.title} · GitLab"
    )

    override def pageReadyElement(implicit webDriver: WebDriver): Option[WebElement] = Some(settingsLink)

    def settingsLink(implicit webDriver: WebDriver): WebElement = eventually {
      find(cssSelector("span.nav-item-name.qa-settings-item")) getOrElse fail("Settings link not found")
    }

    def repositoryLink(implicit webDriver: WebDriver): WebElement = eventually {
      find(cssSelector("#js-onboarding-repo-link")) getOrElse fail("Repository link not found")
    }

    // def projectBranchesButton(implicit webDriver: WebDriver): WebElement = eventually {
    //   find(
    //     //cssSelector(".btn-success")
    //     cssSelector( //s"a[href='$path/environments/new']"
    //       ".project-stats > div > ul > li:nth-child(2) > a"
    //       //s"a.nav-link.stat-link.d-flex.align-items-center[href='$branchesPath']"
    //     )
    //   ) getOrElse fail(
    //     "Advanced -> Branches button not found for: " + branchesPath
    //   )
    // }
  }

  case object GitLabProjectsPage extends GitLabPage {
    override val path:  Path  = "/gitlab/dashboard/projects"
    override val title: Title = "Projects · Dashboard · GitLab"
    override def pageReadyElement(implicit webDriver: WebDriver): Option[WebElement] = Some(newProjectButton)

    private def newProjectButton(implicit webDriver: WebDriver): WebElement = eventually {
      find(cssSelector("a[href='/gitlab/projects/new']")) getOrElse fail("New Project button not found")
    }
  }

  case object SettingsPage extends GitLabPage {

    override val path: Path = Refined.unsafeApply(
      s"/gitlab/${userCredentials.userNamespace}/${projectDetails.title.toPathSegment}/edit"
    )

    override val title: Title = Refined.unsafeApply(
      s"General · Settings · ${userCredentials.fullName} / ${projectDetails.title} · GitLab"
    )

    override def pageReadyElement(implicit webDriver: WebDriver): Option[WebElement] = Some(Advanced.expandButton)

    object Advanced {

      def expandButton(implicit webDriver: WebDriver): WebElement = eventually {
        find(cssSelector("section.advanced-settings button.js-settings-toggle")) getOrElse fail(
          "Advanced -> Expand button not found"
        )
      }

      def removeProject(implicit webDriver: WebDriver): WebElement = eventually {
        find(cssSelector("form > input[value='Remove project'")) getOrElse fail(
          "Advanced -> Remove project button not found"
        )
      }

      def confirmRemoval(project: ProjectDetails)(implicit webDriver: WebDriver): Unit = eventually {
        find(cssSelector("input#confirm_name_input"))
          .getOrElse(fail("Advanced -> Project removal name confirmation input not found"))
          .enterValue(project.title.toPathSegment)

        find(cssSelector("input[value='Confirm'"))
          .getOrElse(fail("Advanced -> Project removal Confirm button not found"))
          .click()
      }
    }
  }

  case object ProjectBranchesPage extends GitLabPage {

    override val path: Path = Refined.unsafeApply(
      s"/gitlab/${userCredentials.userNamespace}/${projectDetails.title.toPathSegment}/-/branches"
    )

    override val title: Title = Refined.unsafeApply(
      s"General · Branches · ${userCredentials.fullName} / ${projectDetails.title} · GitLab"
    )

    override def pageReadyElement(implicit webDriver: WebDriver): Option[WebElement] = Some(newBranchButton)

    def newBranchButton(implicit webDriver: WebDriver): WebElement = eventually {
      find(cssSelector(s"a[href='$path/new']")) getOrElse fail("New Branch button not found")
    }
  }

  case object NewBranchPage extends GitLabPage {

    override val path: Path = Refined.unsafeApply(
      s"/gitlab/${userCredentials.userNamespace}/${projectDetails.title.toPathSegment}/-/branches/new"
    )

    override val title: Title = Refined.unsafeApply(
      s"General · New Branch · ${userCredentials.fullName} / ${projectDetails.title} · GitLab"
    )

    override def pageReadyElement(implicit webDriver: WebDriver): Option[WebElement] = Some(createButton)

    def submitNewBranchForm(
        )(implicit webDriver: WebDriver, browser: WebBrowser with Driver, captureScreenshots: Boolean = false): Unit =
      eventually {
        nameField.clear() sleep (1 second)
        nameField.enterValue("test_branch") sleep (1 second) //maybe add a random number here..

        createButton.click() sleep (1 second)
      }

    private def nameField(implicit webDriver: WebDriver): WebElement = eventually {
      find(cssSelector("input#branch_name")) getOrElse fail("Title field not found")
    }

    def createButton(implicit webDriver: WebDriver): WebElement = eventually {
      find(cssSelector("#new-branch-form > div.form-actions > button")) getOrElse fail("Create button not found")
    }
  }
}

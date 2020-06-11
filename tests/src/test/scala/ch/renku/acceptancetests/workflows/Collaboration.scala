package ch.renku.acceptancetests.workflows

import ch.renku.acceptancetests.model.projects.ProjectDetails
import ch.renku.acceptancetests.pages._
import ch.renku.acceptancetests.tooling.AcceptanceSpec
import ch.renku.acceptancetests.pages.GitLabPages.GitLabBaseUrl
import org.openqa.selenium.{WebDriver, WebElement}
import scala.jdk.CollectionConverters._
import org.scalatestplus.selenium.WebBrowser.{cssSelector, find}
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.concurrent.duration._

trait Collaboration {
  self: AcceptanceSpec =>

  def verifyMergeRequestsIsEmpty(implicit projectDetails: ProjectDetails): Unit = {
    val projectPage = ProjectPage()
    When("the user navigates to the Collaboration tab")
    click on projectPage.Collaboration.tab
    And("they navigate to the Merge Requests sub tab")
    click on projectPage.Collaboration.MergeRequests.tab
    Then("they should see a 'No merge requests to display' info")
    verify userCanSee projectPage.Collaboration.MergeRequests.noMergeRequests
  }

  def verifyIssuesIsEmpty(implicit projectDetails: ProjectDetails): Unit = {
    val projectPage = ProjectPage()
    When("the user navigates to the Collaboration tab")
    click on projectPage.Collaboration.tab
    And("they navigate to the Issues sub tab")
    click on projectPage.Collaboration.Issues.tab
    Then("they should see a 'No issues to display' info")
    verify userCanSee projectPage.Collaboration.Issues.noIssues
  }

  def createNewIssue(implicit projectDetails: ProjectDetails): Unit = {
    implicit val projectPage = ProjectPage()
    When("the user navigates to the Collaboration tab")
    click on projectPage.Collaboration.tab
    And("they navigate to the Issues sub tab")
    click on projectPage.Collaboration.Issues.tab
    // Give some time for the tab change to take effect
    sleep(1 second)
    And("the user clicks on the 'New Issue' button")
    click on projectPage.Collaboration.Issues.newIssueLink

    And("they fill out the form")
    val issueTitle = "test issue"
    val issueDesc  = "test description"
    `create an issue with title and description`(issueTitle, issueDesc)

    Then("the new issue should be displayed in the list")
    val issueTitles = projectPage.Collaboration.Issues.issueTitles
    if (issueTitles.size < 1) fail("There should be at least one issue")
    issueTitles.find(_ == issueTitle) getOrElse fail("Issue with expected title could not be found.")
  }

  def `create an issue with title and description`(title:       String,
                                                   description: String)(implicit projectPage: ProjectPage): Unit = {
    val tf = projectPage.Collaboration.Issues.NewIssue.titleField
    tf.clear() sleep (1 second)
    tf enterValue title sleep (1 second)
    projectPage.Collaboration.Issues.NewIssue.markdownSwitch click;
    projectPage.Collaboration.Issues.NewIssue.descriptionField enterValue description sleep (1 second)
    projectPage.Collaboration.Issues.NewIssue.createIssueButton click;
    sleep(3 seconds)
  }

  // def addBranchToProject(implicit projectDetails: ProjectDetails, gitLabBaseUrl: GitLabBaseUrl): Unit = {
  //   val projectPage = ProjectPage()
  //   When("the user clicks on the 'View in GitLab'")
  //   click on projectPage.viewInGitLab sleep (1 second)
  //   Then("a new tab with GitLab page should open")
  //   val gitLabPages = GitLabPages()
  //   verify browserSwitchedTo gitLabPages.ProjectPage
  //   When("the user navigates to the Project Settings")
  //   go to gitLabPages.SettingsPage
  //   verify browserSwitchedTo gitLabPages.SettingsPage
  //   // And("they click on the Expand button in the Advanced section")
  //   // click on gitLabPages.SettingsPage.Advanced.expandButton sleep (1 second)
  //   // And("they click on the Remove project button")
  //   // click on gitLabPages.SettingsPage.Advanced.removeProject sleep (1 second)
  //   // And("they confirm the project removal")
  //   // gitLabPages.SettingsPage.Advanced confirmRemoval projectDetails sleep (1 second)
  // }

  def addBranchToProjectInGitLab(implicit projectDetails: ProjectDetails, gitLabBaseUrl: GitLabBaseUrl): Unit = {
    val projectPage = ProjectPage()
    When("the user clicks on the 'View in GitLab' button")
    click on projectPage.viewInGitLab sleep (3 seconds)
    Then("a new tab with GitLab page should open")
    val gitLabPages = GitLabPages()
    verify browserSwitchedTo gitLabPages.ProjectPage sleep (1 second)
    Then("the user navigates to the Branches Page")
    go to gitLabPages.ProjectBranchesPage
    verify browserSwitchedTo gitLabPages.ProjectBranchesPage sleep (1 second)
    Then("the user navigates to the the New Branch Page")
    go to gitLabPages.NewBranchPage
    verify browserSwitchedTo gitLabPages.NewBranchPage sleep (1 second)
    Then("the fills in the new branch form")
    // And("they click on the Expand button in the Advanced section")
    // click on gitLabPages.SettingsPage.Advanced.expandButton sleep (1 second)

    // val tabs = webDriver.getWindowHandles.asScala.toArray
    // webDriver.switchTo() window tabs(1)
    // And("click on the project branches button")
    // verify browserSwitchedTo gitLabPages.ProjectPage
    // Then("the browser switched to gitlab project")
    // click on gitLabPages.ProjectPage.projectBranchesButton sleep (4 seconds)
    // And("the user clicks on the Branches link")
    // verify browserSwitchedTo gitLabPages.ProjectBranchesPage
    // Then("a the user is redirected to the branches page for the project")
    // verify browserSwitchedTo gitLabPages.ProjectBranchesPage
    // When("the user clicks on the 'New Branch' button")
    // click on gitLabPages.ProjectBranchesPage.newBranchButton sleep (4 seconds)
    // go to gitLabPages.NewBranchPage
    // // Then("the user is redirected to the new branch form for the project")
    // // go to gitLabPages.ProjectBranchesPage sleep (2 seconds)
    // // verify browserSwitchedTo gitLabPages.ProjectBranchesPage
    // // Then("the user is redirected to the new branch form for the project")
    // // go to gitLabPages.NewBranchPage sleep (2 seconds)
    // verify browserSwitchedTo gitLabPages.NewBranchPage
    // Then("the branch gets created.")
    // gitLabPages.NewBranchPage.submitNewBranchForm() sleep (1 second)
  }

  def verifyBranchWasAdded(implicit projectDetails: ProjectDetails): Unit = {
    val projectPage = ProjectPage()
    When("the user navigates to the Collaboration tab")
    click on projectPage.Collaboration.tab
    Then("they should see a 'Do you want to create a merge request for branch...' banner")
    verify userCanSee projectPage.Collaboration.MergeRequests.futureMergeRequestBanner
  }

  // def goToGitLabNewBranch(implicit webDriver: WebDriver): WebElement = eventually {
  //   find(cssSelector("#content-body > div.top-area.adjust > div > a.btn.btn-success"))
  //     .getOrElse(fail("Advanced -> Project removal Confirm button not found"))
  //     .click()
  // }

  // def goToGitlabBranches(implicit webDriver: WebDriver): WebElement = eventually {
  //   find(
  //     cssSelector(
  //       "#content-body > div.limit-container-width > div.js-show-on-project-root.project-home-panel > nav > div > ul > li:nth-child(2) > a"
  //     )
  //   ).getOrElse(fail("Advanced -> Project removal Confirm button not found"))
  //     .click()
  // }

}

# QMCPACK ORNL CI Setup

# install Jenkins
Following https://jenkins.io/doc/book/installing/#fedora

set qmcpack pw: XXXXXXXX
email: nobody@oxygen.ornl.gov

# configure Jenkins

## Manage Jenkins, Manage Plugins
Install Plugins in addition to the "standard" plugins

- Blue Ocean
- Blue Ocean Executor Info
- Email Extension Plugins
- Github Custom Notification Context SCM Behavior
- PAM Authentication Plugin
- Pipeline: API
- Pipeline: Declarativ
- Pipeline: Github
- Pipeline: Github Groovey Libraries

## Manage Jenkins, Configure System

- set the number of executors to desired number (e.g. 1) and label with e.g.
  "master"
- under "Jenkins Location"
    - change "System Admin e-mail address" to something like "jenkins on oxygen
      <nobody@oxygen.ornl.gov>"

- under "Extended E-mail Notification"
    - change "SMTP server" to "smtp.ornl.gov"
    - check "Allow sending to unregistered users"

- create a github "personal access token" for the robot user
    - check all of "repo" category, and "user:email"

## Adding Pipelines

- From the main dashboard click on *Open Blue Ocean*
- Click New Pipeline
- Answer questions
  - We store our code on GitHub
  - The organization is QMCPACK
  - The repository is qmcpack
  - Choose a descriptive name
- Now click jenkins at the top of the page
- Click the outgoing box icon in the top right
- You'll return to the "classic UI"
- Click the name of your new pipline
- Click Configure


- under "Source Code Management"
    - click the "Git" radio button
    - "https://github.com/QMCPACK/qmcpack/" for the "Repository URL"
    - "none" for Credentials
    - click the Advanced button
    - "+refs/pull/*:refs/remotes/origin/pr/*" for the "Respec"
    - use "${sha1}" for "Branch Specifier (blank for 'any')"
    - choose "Advanced clone behaviors" from the "Additional Behaviors" menu
        - uncheck "Fetch tags"
        - check "Shallow clone" and set depth to "1"

- under "Build Triggers"
    - check "GitHub Pull Request Builder"
    - leave it as "Anonymous connection"
    - add to "Admin list" the following:
        prckent
        markdewing
        ye-luo
        grahamlopez
        PDoakORNL
        rcclay
    - click the "Advanced" button
        - change "Crontab line" to "H/2 * * * *"
    - click the "Trigger Setup" button
        - Add "Update commit status during build" and set the "Commit Status
          Context" to something like "oxygen-cpu"

- under "Build Environment"
    - check "Delete workspace before build starts"

- under "Build"
    - Add "Execute shell"
    - put something like "./tests/test_automation/jenkins_oxygen_cpu.sh" in the box

- under "Post-build Actions"
    - add "Editable Email Notification" action
    - select "Attach Build Log"
    - click "Advanced Settings..." button
    - click the "Advanced" button under "Failure - Any"
    - click the red "X" on the "Developers" box if present
    - add "${ENV, var="ghprbActualCommitAuthorEmail"}, lopezmg@ornl.gov, kentpr@ornl.gov, markdewing@gmail.com, yeluo@anl.gov, moralessilva2@llnl.gov, doakpw@ornl.gov"
      to the "Recipient List" box
    - select "Attach Build Log"
    - add "Delete workspace when build is done" action

## other variants

- click "New Item", and use the "Copy from" box at the bottom to clone an
  existing project

- the only change should be
    - under "Build Triggers"
        - click the "Trigger Setup" button
        - Add "Update commit status during build" and set the "Commit Status
          Context" to something like "oxygen-variant"
    - under "Build"
        - put something like "./tests/test_automation/jenkins_oxygen_variant.sh" in the box

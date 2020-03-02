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

### In your editor
- Prepare the variant Jenkinsfile
- it should be in tests/test_automation and names Jenkinsfile_facility_machine_your_variant
- it should contain
```groovy
library 'qmcpack_shared_jenkins'

common_pipeline(name='your_variant')
```
- commit and push this to your PR Branch

### Back in your browser
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
- under Build Configuration, it should have Mode by Jenkinsfile
- add Script Path tests/test_automation/Jenkinsfile_ornl_oxygen_your_variant
- under Pipeline Libraries fill in
  - Name: qmcpack_shared_jenkins
  - Default Version: master
  - Load Implicitly: check
  - Retrieval Method: Modern SCM
  - Source Code Management: Github
  - Credentials: BlueOcean Folder Credentials
  - Repository HTTPS URL: https://github.com/QMCPACK/qmcpack_shared_jenkins.git
  - Validate that, it should work if you created the auth token for qmc-robot properly
  - at the bottom of the Behaviors section click the Add button
  - select Custom Github Notification Context
  - Label it too something you want to see for the PR checks on github.
  - Scroll to the bottom and click Save

### In Your Editor
- if there is not already a `jenkins_facility_machine.sh` script prepare one based on `jenkins_ornl_oxygen.sh`
- prepare the facility_machine_spack_env.sh file that loads the correct spack packages 
  and sets the QMC_IMMUTABLE_FLAGS for your build.


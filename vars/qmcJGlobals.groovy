#!groovy

/** Global variables for the QMCPACK CI setup.  This avoids having to
  * set them up in the web interface and track them trough web forms
  *
  *  Take care that even strings defined with GString do not work
  *  for string globals. 
  */
class gmcJGlobals
{
  static def maintainer_emails = "lopezmg@ornl.gov, kentpr@ornl.gov, markdewing@gmail.com, yeluo@anl.gov, moralessilva2@llnl.gov, doakpw@ornl.gov"
}

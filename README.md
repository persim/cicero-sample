# cicero-sample
This repository contains a basic example on how configure and use Cicero. For more information about Cicero, visit the main <a href="http://www.icta.ufl.edu/opensource.htm/cicero">website</a>.

# Setup
This sample project is ready to use and Cicero is already installed as a dependency module.<br/>
In order to install Cicero in an existing project, please follow the below indications.
<ul>
	<li>Download Cicero;</li>
	<li>Import the dowloaded .aar file like a new module, following File > New > New Module > Import aar/jar package > select dowloaded file;</li>
	<li>Add the folliwing lines into the app’s gradle file inside dependences section:<br><code>//Add Cicero to dependencies</code><br><code>compile project(':cicero-v1.0-release')</code><br><code>//Google Play Services is needed</code><br><code>compile 'com.google.android.gms:play-services-location:8.4.0'</code></li>
	<li>Start using <code>CyberManager</code>'s methods inside your app code.
</ul>

# Javadoc
You can browse the full documentation <a href="http://www.icta.ufl.edu/opensource.htm/cicero/javadoc/index.html">here</a>.


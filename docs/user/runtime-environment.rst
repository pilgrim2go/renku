.. _runtime_env:

Runtime environment in Renku
============================

Renku allows you to customize your runtime environment and reuse it in other projects. If you are programming in python the `requirements.txt <_python_environment>`_ file is the conventional way to specify the required libraries. If however python is not your thing, or if the capabilities of ``requirements.txt`` file are too limited for your special requirements, you can further customize your runtime environment to include other programming languages and libraries.

Before going into the details of how this can be done, we first need to understand how user applications run in Renku.
When you work remotely on a project, your work is executed on the Renku platform into a container that performs operating-system-level virtualization. Containers consists of an entire runtime environment that have everything the software needs to run, including libraries, system tools, and code. This means that even if containers share the same physical underlying compute infrastructure, CPU, memory, and disk space, they run separate operating systems and are oblivious of one another. You may configure your respective containers anyway you want.

Prerequesits
------------

This section assumes that you know the basics of git terminology, and have minimum knowledge of software containerization.

Customizing your runtime environment
------------------------------------

Renku containers are based on the popular Docker technology. Docker containers are spawn from images. You assemble images using the same command you would use to prepare a linux-based environment.
Docker images are built by reading the assembly instructions from a `Dockerfile <https://docs.docker.com/engine/reference/builder/>`_. A Dockerfile is a text document that contains all the commands a user would call on the command line to assemble an image.
Renku includes a default Dockerfile in the project repository, and automatically builds the image when it detects a new version of this Dockerfile or a new version one of its dependencies when new commits are pushed.
You can see the image building process in action by opening the project's gitlab view in Renku, and selecting the `CI/CD` tab in the left panel. If you also visit the `Registry` tab, you will find that each image is stored in the project's docker registry and images are tagged with the commit id (`Tag` field). Note that each commit id is associated with an image, and the same image (same `Tag ID` field) can be used in multiple commit id.
Keeping the Dockerfile under version control and tagging images with the commit id, is Renku's way to maintain consistent associations between code versions, data versions, and runtime environment versions. More specifically, it allows you or other users to go back in time and recover the image containing the appropriate runtime configuration needed to recreate a dataset.

- Troubleshooting

Using your customized runtime environment
-----------------------------------------

- Spawning a Docker container
- Troubleshooting

Sharing your runtime environment
--------------------------------

- Forking a project (and gotchas)
- Gitlab docker registry
- Reusing a docker image from another project

Where to go from here
---------------------

.. _Docker: https://docs.docker.com/engine/reference/builder/
.. _Gitlab CI/CD: https://docs.gitlab.com/ee/ci/introduction/


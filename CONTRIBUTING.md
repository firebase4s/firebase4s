# Contributing to Firebase4s

The Firebase4s project welcomes contributions from anybody wishing to participate.
All code or documentation that is provided must be licensed with the same
license that Firebase4s is licensed with (MIT, see LICENSE.txt).

If you'd like to chat about the project, join us on [gitter](https://gitter.im/firebase4s/firebase4s).

## Roadmap

Firebase4s is new project, and there is still a lot to do, including:

 - An improved README
 - Additional documentation and examples
 - Reaching feature parity with the [Firebase Java SDK](https://firebase.google.com/docs/admin/setup)
 - Enabling the use of `case classes` with the Database via Scala macros
 - More thorough unit tests
 - Continuous Integration

## General Workflow

1. Make sure you can license your work under the MIT license.

2. If there is something that you'd like to work on, please make sure
   there is an existing issue or create a new one.  There should be
   a consensus about the issue before starting work.

3. You should do your work in a local branch of your own fork and
   then submit a pull request.

4. When the work is completed, submit a Pull Request.

5. Anyone can comment on a pull request and you are expected to
   answer questions or to incorporate feedback.

6. It is not allowed to force push to the branch on which the pull
   request is based.

## General Guidelines

1. It is recommended that the work is accompanied by relevant unit
   tests.

2. All unit tests should be passing. To run tests with sbt:

   ```bash
    > project test
    > test
   ```

3. The commit messages should be short and clear.  If additional
   details are required, provide them in the body of PR.

4. Follow the structure of the code in this repository and the
   formatting using the included scalafmt.conf.

5. Your first commit request should be accompanied with a change to
   the AUTHORS file, adding yourself to the authors list.


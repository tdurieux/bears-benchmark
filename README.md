# CSSE Backend [![Build Status](https://travis-ci.org/SE3070-CSSE/csse-backend.svg?branch=development)](https://travis-ci.org/SE3070-CSSE/csse-backend)

- Documentation of endpoints in [Swagger](https://procurement-system.herokuapp.com/swagger-ui.html)

## Git Workflow

This document describes the workflow, conventions, and best practices to use with regard to Git.For those of you who are new to git, or want a refresher, the following links will help

- [Atlassian git tutorials](https://www.atlassian.com/git/) - Covers a wide range of git topics
- [git - the simple quide](http://rogerdudler.github.io/git-guide/) - A more simplified guide


### Branching

Every new feature, bug-fix, spelling correction, or change should be developed on a separate branch.

`master` is the branch that gets deployed to production, so it should always be in a production-ready state (i.e. all tests should pass). 

##### Branch naming

Branch names should be in **kebab-case** (use hyphens to separate words, all simple). Use descriptive branch names.

(#TODO : correct format for bug fixes?) // damsith

Examples: 

    Good:

        #20-create-service-for-items

    Bad:

        no_form_validation (uses snake-case instead of kebab-case)
        errors (not descriptive)
        fix-bug (not descriptive)

### Committing

A commit should contain one **self-contained**, **reversible**, readable change to your code. This has numerous benefits:

- If you want to undo a change, you can revert a specific commit. If a commit contains multiple changes, you will have to *manually undo individual changes* which can be a nightmare and a waste of time.
- You can easily describe the changes in the commit message. This makes it easy to find the commit where a change was made.
- A reviewer (or anybody reading the code) can easily see which pieces of code are relevant to a change by looking at the commit for that change. If a commit contains multiple changes, it is often unclear which pieces of code are relevant to which change.

When committing, it's a good idea to review every line of code that you commit. Even if you have multiple conceptual changes implemented, craft your commits such that each commit only contains one change. A great way to do this is to use `git add -p`, which breaks your changes up into individual patches and allows you to interactively choose which ones to stage.

When writing commit messages, the first line should be a short description of the change. Since you only have one conceptual change in your commit, it should be easy to describe in one line, right? You may want to prefix this line with the issue ID followed by a colon (e.g. `1128: added validation for email`). Use the rest of the commit message to expand on the context of the change so that it is easier to understand.


#### Whitespace changes and prettyfying
Whitespace changes and renaming/moving files are special cases and should be treated differently. In particlar, it can be diffiult to review code where there are large diffs that move chunks of code around.
 - git (and github) sometimes has trouble displaying changes when a file is both renamed and edited. Instead, make one commit for renaming/moving the files, and a second for the code changes.
 - When making signifiant whitespace changes/reflowing code, it's best to make one commit containing only whitespace changes (pro tip: pulls/<#>/files?w=1), and a second one containing the code changes.
By splitting the changes into commits that clearly change specific things (and making a note in the PR), it helps the reviewer go through the changes by commit, and make the mental switch to be looking for issue as appropriate, rather than having to confirm that everything is the same as before, except what was moved.

Good: TODO
Bad: TODO

### Pull Requests

We use the [shared repository model](https://help.github.com/articles/about-collaborative-development-models/) where collaborators are granted push access to a single shared repository and topic branches are created when changes need to be made. Pull requests are useful in this model as they initiate code review and general discussion about a set of changes before the changes are merged into the main development branch

The description of a pull request must contain the following things:

- **What does the change do?** Typically this would be a one line description that goes in the "subject" of the PR
- **Why is this change needed?** Provide a context around what motivates this change. This could be
    * an explanation inside the description or
    * a reference to a discussion in some other place like a mailing list, JIRA issue etc. Not only is it helpful for the reviewer, it can also be used to figure out why a particular change was made when looking back.
- How is the change tested? Since you should be [[[[testing all your changes]]]], provide that information in the PR description. This has two
benefits. 
    1. It assures the reviewer that change indeed works correctly and he/she can prescribe
any additional tests if needed. 
    2. It can also be used as a guideline for someone else making changes
to the same parts of the codebase.

For more good advice on how to write and review a PR, see [Github's guidelines](https://github.com/blog/1943-how-to-write-the-perfect-pull-request).

### Workflow

Here is the simplest development workflow you should use:

**Step 0**. Get the latest version of `master`.

        git checkout master
        git pull

**Step 1**.  Make a new branch off of `master`. The branch name should start with a '#' followed by the issue number and description in kebab case.

        git checkout -b #12-my-great-feature

**Step 2**. Implement your changes, pushing your commits along the way. (It is advisable to use git add -p instead of git add . to have fine control over what goes into a commit)

        git add -p  
        git commit
        git push

    You should push early and often in order to ensure that the most up to date code is on Github - remember, use [github as a filesystem](#github-as-filesystem). That can mean pushing after every commit, or pushing every time you stop a work session. At the bare minimum, push before you stop for the day.

    If you are developing over a long period of time, and `master` is changing, you should merge `master` into your branch often to make sure it stays up to date. This will reduce merge conflicts when you finally merge your branch back into `master`.

        git checkout master
        git pull
        git checkout my-great-feature
        git merge master

**Step 3**. When you are ready to start having your code reviewed, open a pull request (PR) and assign it to a reviewer.

    **As a reviewer**, use GitHub comments to communicate your feedback on the PR.
    Assign it to the requester after every batch of comments.
    Finally, assign it to the requester with an LGTM to sign off on the PR.

    **As a requester**, make changes based on the reviewer's comments.
    Respond to comments with the SHA of the commit that address the comment so that both you and the reviewer can make sure every
    comment is addressed.
    Once you have addressed the comments assign the PR back to the reviewer.
    Repeat this until the reviewer no longer has comments.

**Step 4**. Once the reviewer signs off on the PR, merge the pull request on github.

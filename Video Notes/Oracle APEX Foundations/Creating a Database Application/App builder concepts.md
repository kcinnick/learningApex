App Builder Concepts:

- APEX application
- Application Page
- Application User Interface
- Session State
- Page Processing & Page Rendering
- Application URL Syntax

What is an APEX application?

A: An HTML interface that exists on top of a database. Collection of pages linked together. Think Oracle Product Information Manager.

Application page:

A page is the building block of an application. APEX applications are a collection of Appliucation pages. Pages are dynamically rendered based on the data in the database.

On a page, you can perform calculations, display reports, charts, etc.

View & edit pages in Page Designer.

APEX engine relies on two processes:
- Show Page, a page rendering process that assembles page attributes into a viewable HTML page
- Accept Page, which performs forms processing/computations/validations/etc. When you submit a page, Accept Page is running.  You can use conditions to control how/when pages appear or execute

Application UI:

- a way to build a responsive app without knowing JS/CSS/HTML
- Key advantages: 
1. responsive design (plays well with mobile/desktop)
2. good UI
3. 'easily' customizable


Session state management:

Session state enables developers to store & retrieve values no matter what page the user is on

APEX maintains session state in the background.

i.e. - to access values entered on one page on the next one, you have to maintain session state.

APEX establishes identity of the user for each page request and uses the the session ID to fetch the session state.

A 'session' is just a way to establish persistence across different page views

So a session is basically created when you first interact with the app until your interaction with that app ends.

Sessions are NOT the same/distinct from Oracle DB sessions used to get data to a page.

URL Syntax:

- APEX supports 'friendly' URL syntax and f?p Syntax.  Depends on which Apex replease you use - basically, new ones will use friendly URL syntax.

Friendly URL syntax == 'normal' http syntax with normal parameters and paths

hostname:port/orders/r/path_prefix/app_alias/page_alias?parameters

i.e. - hostname:port/ords/r/mycompany/hr-app/update-employees?session=1

/r/ is a router shortcut and will always be there.




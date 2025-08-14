## Breaking Changes

* The parsing of query URLs has changed such that the '?' character should strictly be treated as the delimiter between path and query components. Any additional '?' characters within the query string must be percent-encoded (e.g. `%3F`). URLs containing unescaped '?' characters in the query portion may cause failures or unexpected behaviour.  

#!/bin/bash

# publish-if-new.sh
# Publishes this package to npm unless its version is already on the registry.
# Run through `npm run publish-if-new`, which supplies npm_package_name and npm_package_version.
#
# npm never allows a version to be republished, and this module builds before the Maven Central
# upload, so a plain `npm publish` would fail any redeploy of a release stage once the package is out.
# An existing version only counts as done if it was published from the commit being built. Anything
# else means the registry holds different content under this version, which needs a person to look at.

set -eu

spec="${npm_package_name}@${npm_package_version}"

# Output is checked rather than exit status, since npm versions disagree on the exit status for a
# missing version. A failed lookup yields empty output and falls through to publishing, which then
# fails loudly instead of being silently skipped.
published_version=$(npm view "$spec" version 2>/dev/null || true)
if [ -z "$published_version" ]; then
    echo "$spec is not on the registry, publishing"
    npm publish --access public
    exit 0
fi

published_commit=$(npm view "$spec" gitHead 2>/dev/null || true)
current_commit=$(git rev-parse HEAD)

if [ "$published_commit" = "$current_commit" ]; then
    echo "$spec is already published from commit $current_commit, skipping"
    exit 0
fi

echo "$spec is already published, but from commit ${published_commit:-<unknown>} rather than $current_commit"
echo "inspect the published package before continuing, this version can never be republished"
exit 1

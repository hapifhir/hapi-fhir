#!/bin/bash

# Publishes the typescript packages to npm unless its version is already on the registry.
#
# npm publishing happens before artifacts are published to sonatype and npm never allows a version
# to be republished. If the pipeline fails on sonatype, a retry will result in a failed npm publish.

set -eu

spec="${npm_package_name}@${npm_package_version}"

# Try publishing if it's a new version or if the version check returned null
published_version=$(npm view "$spec" version 2>/dev/null || true)
if [ -z "$published_version" ]; then
    echo "$spec is not on the registry, publishing"
    npm publish --access public
    exit 0
fi

published_commit=$(npm view "$spec" gitHead 2>/dev/null || true)
current_commit=$(git rev-parse HEAD)

# The version exists, and the commit SHA is the same. Simple skip.
if [ "$published_commit" = "$current_commit" ]; then
    echo "$spec is already published from commit $current_commit, skipping"
    exit 0
fi

# The version exists, and the commit SHA is different. Flag and exit.
echo "$spec is already published, but from commit ${published_commit:-<unknown>} rather than $current_commit"
echo "inspect the published package versions before continuing, this version can never be republished"
exit 1

#!/bin/sh

# Decrypt the file
# --batch to prevent interactive command --yes to assume "yes" for questions
gpg --quiet --batch --yes --decrypt --passphrase="$RENKUBOT_GPG_PASSPHRASE" \
--output my_secret.txt my_secret.txt.gpg

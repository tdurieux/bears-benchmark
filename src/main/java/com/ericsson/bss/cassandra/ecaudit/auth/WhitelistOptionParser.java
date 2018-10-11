//**********************************************************************
// Copyright 2018 Telefonaktiebolaget LM Ericsson
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//**********************************************************************
package com.ericsson.bss.cassandra.ecaudit.auth;

import java.util.Set;

import org.apache.cassandra.auth.IResource;
import org.apache.cassandra.auth.Permission;
import org.apache.cassandra.exceptions.InvalidRequestException;

public class WhitelistOptionParser
{
    private static final String GRANT_PREFIX = "grant_audit_whitelist_for_";
    private static final String REVOKE_PREFIX = "revoke_audit_whitelist_for_";
    private static final String VALID_PREFIX = "^" + GRANT_PREFIX + "|" + "^" + REVOKE_PREFIX;

    public WhitelistOperation parseWhitelistOperation(String inputOption)
    {
        if (inputOption.startsWith(GRANT_PREFIX))
        {
            return WhitelistOperation.GRANT;
        }
        else if (inputOption.startsWith(REVOKE_PREFIX))
        {
            return WhitelistOperation.REVOKE;
        }
        else
        {
            throw new InvalidRequestException("Invalid whitelist operation option: " + inputOption);
        }
    }

    public Permission parseTargetOperation(String inputOption)
    {
        String operationString = stripOptionPrefix(inputOption).toUpperCase();

        try
        {
            return Permission.valueOf(operationString);
        }
        catch (IllegalArgumentException e)
        {
            throw new InvalidRequestException("Invalid whitelist option: " + e.getMessage());
        }
    }

    private String stripOptionPrefix(String option)
    {
        return option.replaceFirst(VALID_PREFIX, "");
    }

    public Set<IResource> parseResources(String resourceCsv)
    {
        try
        {
            return ResourceFactory.toResourceSet(resourceCsv);
        }
        catch (IllegalArgumentException e)
        {
            throw new InvalidRequestException(String.format("Unable to parse whitelisted resources [%s]: %s", resourceCsv, e.getMessage()));
        }
    }
}

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
import java.util.stream.Collectors;

import org.apache.cassandra.auth.IResource;
import org.apache.cassandra.auth.Permission;
import org.apache.cassandra.exceptions.InvalidRequestException;

public class WhitelistContract
{
    public void verify(Permission operation, Set<IResource> suppliedResources)
    {
        Set<IResource> applicableResources = suppliedResources.stream()
                                                              .filter(r -> r.applicablePermissions().contains(operation))
                                                              .collect(Collectors.toSet());

        if (!applicableResources.containsAll(suppliedResources))
        {
            throw new InvalidRequestException(String.format("%s is not applicable on %s", operation, suppliedResources));
        }
    }

    public void verifyCreateRoleOption(WhitelistOperation whitelistOperation)
    {
        if (whitelistOperation != WhitelistOperation.GRANT)
        {
            throw new InvalidRequestException(String.format("Only possible to grant audit whitelist when creating a role"));
        }
    }
}

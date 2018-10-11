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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.ericsson.bss.cassandra.ecaudit.auth.cache.AuthCache;
import com.ericsson.bss.cassandra.ecaudit.auth.cache.DescriptorBridge;
import org.apache.cassandra.auth.IResource;
import org.apache.cassandra.auth.IRoleManager;
import org.apache.cassandra.auth.Permission;
import org.apache.cassandra.auth.RoleResource;
import org.apache.cassandra.config.DatabaseDescriptor;

public class AuditWhitelistCache extends AuthCache<RoleResource, Map<Permission, Set<IResource>>>
{
    private static final AuditWhitelistCache CACHE = new AuditWhitelistCache(DatabaseDescriptor.getRoleManager());

    private AuditWhitelistCache(IRoleManager roleManager)
    {
        super("AuditWhitelistCache",
              DatabaseDescriptor::setRolesValidity,
              DatabaseDescriptor::getRolesValidity,
              DatabaseDescriptor::setRolesUpdateInterval,
              DatabaseDescriptor::getRolesUpdateInterval,
              DescriptorBridge::setRolesCacheMaxEntries,
              DatabaseDescriptor::getRolesCacheMaxEntries,
              (r) -> splitCustomOptions(roleManager.getCustomOptions(r)),
              () -> DatabaseDescriptor.getAuthenticator().requireAuthentication());
    }

    // TODO: We could skip this if we get Map directly from AuditWhitelistManager
    private static Map<Permission, Set<IResource>> splitCustomOptions(Map<String, String> customOptions)
    {
        return customOptions
               .entrySet()
               .stream()
               .collect(Collectors.toMap(p -> Permission.valueOf(p.getKey()), p -> ResourceFactory.toResourceSet(p.getValue())));
    }

    /**
     * Get all custom options immediately associated with the supplied role. The returned options may be cached if
     * roles_validity_in_ms has a value greater than zero.
     *
     * @param role the Role
     * @return map of all custom options associated with the role
     */
    public static Map<Permission, Set<IResource>> getCustomOptions(RoleResource role)
    {
        try
        {
            return CACHE.get(role);
        }
        catch (ExecutionException e)
        {
            throw new RuntimeException(e);
        }
    }
}

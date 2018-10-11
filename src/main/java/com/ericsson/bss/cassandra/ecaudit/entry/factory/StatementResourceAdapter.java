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
package com.ericsson.bss.cassandra.ecaudit.entry.factory;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.ericsson.bss.cassandra.ecaudit.facade.CassandraAuditException;
import org.apache.cassandra.auth.DataResource;
import org.apache.cassandra.auth.IResource;
import org.apache.cassandra.auth.RoleResource;
import org.apache.cassandra.cql3.statements.AuthenticationStatement;
import org.apache.cassandra.cql3.statements.AuthorizationStatement;
import org.apache.cassandra.cql3.statements.PermissionsManagementStatement;
import org.apache.cassandra.cql3.statements.UseStatement;

public class StatementResourceAdapter
{
    /**
     * Extract the {@link RoleResource} from the {@link AuthenticationStatement}.
     * <p>
     * The abstract type {@link AuthenticationStatement} itself does not contain any member named "role".
     * But all known implementation does have a member with correct name and type.
     *
     * @param statement the statement
     * @return the RoleResource
     */
    public RoleResource resolveRoleResource(AuthenticationStatement statement)
    {
        try
        {
            return (RoleResource) FieldUtils.readField(statement, "role", true);
        }
        catch (IllegalAccessException e)
        {
            throw new CassandraAuditException("Failed to resolve resource", e);
        }
    }

    public IResource resolveManagedResource(PermissionsManagementStatement statement)
    {
        try
        {
            return (IResource) FieldUtils.readField(statement, "resource", true);
        }
        catch (IllegalAccessException e)
        {
            throw new CassandraAuditException("Failed to resolve resource", e);
        }
    }

    public RoleResource resolveGranteeResource(AuthorizationStatement statement)
    {
        try
        {
            RoleResource resource = (RoleResource) FieldUtils.readField(statement, "grantee", true);
            if (resource == null)
            {
                resource = RoleResource.root();
            }
            return resource;
        }
        catch (IllegalAccessException e)
        {
            throw new CassandraAuditException("Failed to resolve resource", e);
        }
    }

    public DataResource resolveKeyspaceResource(UseStatement statement)
    {
        try
        {
            String keyspace = (String) FieldUtils.readField(statement, "keyspace", true);
            return DataResource.keyspace(keyspace);
        }
        catch (IllegalAccessException e)
        {
            throw new CassandraAuditException("Failed to resolve resource", e);
        }
    }
}

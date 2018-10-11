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

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;

import org.apache.cassandra.auth.DataResource;
import org.apache.cassandra.auth.IResource;
import org.apache.cassandra.auth.Permission;
import org.apache.cassandra.exceptions.InvalidRequestException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TestWhitelistContract
{
    WhitelistContract contract;

    @Before
    public void before()
    {
        contract = new WhitelistContract();
    }

    @Test
    public void testGrantSelectOnData()
    {
        Set<IResource> suppliedResources = ImmutableSet.of(DataResource.fromName("data/ks/table"));
        contract.verify(Permission.SELECT, suppliedResources);
    }

    @Test
    public void testRevokeSelectOnData()
    {
        Set<IResource> suppliedResources = ImmutableSet.of(DataResource.fromName("data/ks/table"));
        contract.verify(Permission.SELECT, suppliedResources);
    }

    @Test
    public void testGrantModifyOnData()
    {
        Set<IResource> suppliedResources = ImmutableSet.of(DataResource.fromName("data/ks/table"));
        contract.verify(Permission.MODIFY, suppliedResources);
    }

    @Test
    public void testRevokeModifyOnData()
    {
        Set<IResource> suppliedResources = ImmutableSet.of(DataResource.fromName("data/ks/table"));
        contract.verify(Permission.MODIFY, suppliedResources);
    }

    @Test
    public void testGrantExecuteOnConnections()
    {
        Set<IResource> suppliedResources = ImmutableSet.of(ConnectionResource.fromName("connections"));
        contract.verify(Permission.EXECUTE, suppliedResources);
    }

    @Test
    public void testGrantSelectOnConnections()
    {
        Set<IResource> suppliedResources = ImmutableSet.of(ConnectionResource.fromName("connections"));
        assertThatExceptionOfType(InvalidRequestException.class).isThrownBy(() -> contract.verify(Permission.SELECT, suppliedResources));
    }

    @Test
    public void testGrantOnCreate()
    {
        contract.verifyCreateRoleOption(WhitelistOperation.GRANT);
    }

    @Test
    public void testRevokeOnCreate()
    {
        assertThatExceptionOfType(InvalidRequestException.class)
        .isThrownBy(() -> contract.verifyCreateRoleOption(WhitelistOperation.REVOKE));
    }
}

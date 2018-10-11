package com.ericsson.bss.cassandra.ecaudit.auth;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import org.apache.cassandra.auth.DataResource;
import org.apache.cassandra.auth.FunctionResource;
import org.apache.cassandra.auth.IResource;
import org.apache.cassandra.auth.RoleResource;
import org.assertj.core.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class TestResourceFactory
{
    @Test
    public void testDataRoot()
    {
        IResource resource = ResourceFactory.toResource("data");
        assertThat(resource.getName()).isEqualTo("data");
        assertThat(resource).isInstanceOf(DataResource.class);
    }

    @Test
    public void testDataKs()
    {
        IResource resource = ResourceFactory.toResource("data/ks");
        assertThat(resource.getName()).isEqualTo("data/ks");
        assertThat(resource).isInstanceOf(DataResource.class);
    }

    @Test
    public void testDataTable()
    {
        IResource resource = ResourceFactory.toResource("data/ks/table");
        assertThat(resource.getName()).isEqualTo("data/ks/table");
        assertThat(resource).isInstanceOf(DataResource.class);
    }

    @Test
    public void testDataFail()
    {
        assertThatIllegalArgumentException()
        .isThrownBy(() -> ResourceFactory.toResource("data/ks/table/fail"));
    }

    @Test
    public void testFunctionRoot()
    {
        IResource resource = ResourceFactory.toResource("functions");
        assertThat(resource.getName()).isEqualTo("functions");
        assertThat(resource).isInstanceOf(FunctionResource.class);
    }

    @Test
    public void testFunctionKs()
    {
        IResource resource = ResourceFactory.toResource("functions/ks");
        assertThat(resource.getName()).isEqualTo("functions/ks");
        assertThat(resource).isInstanceOf(FunctionResource.class);
    }

    @Test
    public void testFunctionName()
    {
        IResource resource = ResourceFactory.toResource("functions/ks/func|LongType^FloatType");
        assertThat(resource.getName()).startsWith("functions/ks/func");
        assertThat(resource).isInstanceOf(FunctionResource.class);
    }

    @Test
    public void testFunctionFail()
    {
        assertThatIllegalArgumentException()
        .isThrownBy(() -> ResourceFactory.toResource("functions/ks/func/fail"));
    }

    @Test
    public void testRoleRoot()
    {
        IResource resource = ResourceFactory.toResource("roles");
        assertThat(resource.getName()).isEqualTo("roles");
        assertThat(resource).isInstanceOf(RoleResource.class);
    }

    @Test
    public void testRoleName()
    {
        IResource resource = ResourceFactory.toResource("roles/bob");
        assertThat(resource.getName()).isEqualTo("roles/bob");
        assertThat(resource).isInstanceOf(RoleResource.class);
    }

    @Test
    public void testRoleLongName()
    {
        IResource resource = ResourceFactory.toResource("roles/bob/with/many/parts");
        assertThat(resource.getName()).isEqualTo("roles/bob/with/many/parts");
        assertThat(resource).isInstanceOf(RoleResource.class);
    }

    @Test
    public void testConnectionRoot()
    {
        IResource resource = ResourceFactory.toResource("connections");
        assertThat(resource.getName()).isEqualTo("connections");
        assertThat(resource).isInstanceOf(ConnectionResource.class);
    }

    @Test
    public void testConnectionFail()
    {
        assertThatIllegalArgumentException()
        .isThrownBy(() -> ResourceFactory.toResource("connections/specific"));
    }

    @Test
    public void testInvalidResourceType()
    {
        assertThatIllegalArgumentException()
        .isThrownBy(() -> ResourceFactory.toResource("unknown"));
    }

    @Test
    public void testStringCsvToResourceSet()
    {
        Set<IResource> expectedResourceSet = ImmutableSet.of(ConnectionResource.fromName("connections"), DataResource.fromName("data/ks"));
        assertThat(ResourceFactory.toResourceSet("connections, data/ks")).isEqualTo(expectedResourceSet);
    }

    @Test
    public void testStringArrayToResourceSet()
    {
        Set<IResource> expectedResourceSet = ImmutableSet.of(ConnectionResource.fromName("connections"), DataResource.fromName("data/ks"));
        assertThat(ResourceFactory.toResourceSet(Arrays.array("connections", "data/ks"))).isEqualTo(expectedResourceSet);
    }

    @Test
    public void testStringSetToResourceSet()
    {
        Set<IResource> expectedResourceSet = ImmutableSet.of(ConnectionResource.fromName("connections"), DataResource.fromName("data/ks"));
        assertThat(ResourceFactory.toResourceSet(ImmutableSet.of("connections", "data/ks"))).isEqualTo(expectedResourceSet);
    }

    @Test
    public void testResourceSetToStringCsv()
    {
        String expectedCsv = "data/ks,connections";
        assertThat(ResourceFactory.toNameCsv(ImmutableSet.of(ConnectionResource.fromName("connections"), DataResource.fromName("data/ks")))).isEqualTo(expectedCsv);
    }

    @Test
    public void testCsvBoot()
    {
        Set<IResource> resourceSet = ImmutableSet.of(ConnectionResource.fromName("connections"), DataResource.fromName("data/ks"));
        String resourceCsv = ResourceFactory.toNameCsv(resourceSet);
        assertThat(ResourceFactory.toResourceSet(resourceCsv)).isEqualTo(resourceSet);
    }
}

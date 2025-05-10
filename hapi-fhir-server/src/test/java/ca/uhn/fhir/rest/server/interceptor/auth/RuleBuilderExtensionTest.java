package ca.uhn.fhir.rest.server.interceptor.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class RuleBuilderExtensionTest
{
    @Test
    public void testCustomReadRule()
    {
        RuleBuilder ruleBuilder = new RuleBuilder()
        {
            @Override
            public IAuthRuleBuilderRule allow(String theRuleName)
            {
                return new RuleBuilderRule(PolicyEnum.ALLOW, theRuleName)
                {
                    @Override
                    protected RuleBuilderRuleOp createReadRuleBuilder()
                    {
                        return new RuleBuilderRuleOp(RuleOpEnum.READ)
                        {
                            @Override
                            public IAuthRuleBuilderRuleOpClassifier resourcesOfType(String theType)
                            {
                                return new RuleBuilderRuleOpClassifier(AppliesTypeEnum.TYPES, Collections.singleton(theType))
                                {
                                    @Override
                                    protected IAuthRuleBuilderRuleOpClassifierFinished finished()
                                    {
                                        return finished(new TestRuleImpl(myRuleName));
                                    }
                                };
                            }
                        };
                    }
                };
            }
        };

        List<IAuthRule> rules = ruleBuilder.allow().read().resourcesOfType("Patient").withAnyId().build();
        assertEquals(1, rules.size());
        IAuthRule rule = rules.get(0);
        assertInstanceOf(TestRuleImpl.class, rule);
    }

    @Test
    public void testCustomBulkExportRule()
    {
        List<IAuthRule> rules = new CustomRuleBuilder().allow("Test Rule").bulkExport().groupExportOnAnyGroup().build();
        assertEquals(1, rules.size());
        IAuthRule rule = rules.get(0);
        assertInstanceOf(TestRuleImpl.class, rule);
    }

    private static class CustomRuleBuilder extends RuleBuilder
    {
        @Override
        public CustomRuleBuilderRule allow(String theRuleName)
        {
            return new CustomRuleBuilderRule(PolicyEnum.ALLOW, theRuleName);
        }

        private class CustomRuleBuilderRule extends RuleBuilderRule
        {
            protected CustomRuleBuilderRule(PolicyEnum theRuleMode, String theRuleName)
            {
                super(theRuleMode, theRuleName);
            }

            @Override
            public CustomRuleBuilderBulkExport bulkExport()
            {
                return (CustomRuleBuilderBulkExport) super.bulkExport();
            }

            @Override
            protected CustomRuleBuilderBulkExport createRuleBuilderBulkExport()
            {
                return new CustomRuleBuilderBulkExport();
            }

            private class CustomRuleBuilderBulkExport extends RuleBuilderBulkExport
            {
                public IAuthRuleFinished groupExportOnAnyGroup()
                {
                    TestRuleImpl rule = new TestRuleImpl(myRuleName);
                    addRule(rule);
                    return new RuleBuilderFinished(rule);
                }
            }
        }
    }

    static class TestRuleImpl extends RuleImplOp
    {
        protected TestRuleImpl(String theRuleName)
        {
            super(theRuleName);
        }
    }
}

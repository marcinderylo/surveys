package org.adaptiveplatform.surveys.domain;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

/**
 *
 * @author Marcin Deryło
 */
@Service
public class ResearchTestFixtureBuilder {

    @Resource
    private SessionFactory sf;
    private final Set<Long> researchIds = new HashSet<Long>();

    public Research createResearch(ResearchBuilder builder) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        Research research = builder.build();
        Long researchId = (Long) session.save(research);
        session.flush();
        tx.commit();
        session.close();
        researchIds.add(researchId);
        return research;
    }

    public void addPublicationToResearch(Long researchId, Long publicationId) {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        Research research = (Research) session.get(Research.class, researchId);
        SurveyPublication publication = (SurveyPublication) session.get(
                SurveyPublication.class, publicationId);
        research.addSurveyPublication(publication);
        session.flush();
        tx.commit();
        session.close();
    }

    public void cleanUp() {
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();

        for (Long id : researchIds) {
            session.createQuery("delete from AnswerEvaluation ae where "
                    + "ae.research.id = :id").setParameter("id", id).executeUpdate();
            Research research = (Research) session.get(Research.class, id);
            session.delete(research);
        }
        tx.commit();
        session.close();
        researchIds.clear();
    }
}

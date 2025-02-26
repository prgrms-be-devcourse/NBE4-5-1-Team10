import RequireAnonymous from "@/components/auth/require-anonymous";
import ClientPage from "./client-page";

export default function Page() {
  return (
    <RequireAnonymous>
      <ClientPage />
    </RequireAnonymous>
  );
}

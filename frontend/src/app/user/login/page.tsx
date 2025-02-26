import RequireAnonymous from "@/components/auth/require-anonymous";
import ClientPage from "./client-page";

export default function LoginPage() {
  return (
    <RequireAnonymous>
      <ClientPage />
    </RequireAnonymous>
  );
}
